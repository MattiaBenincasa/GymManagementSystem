package BusinessLogicTest.Purchase;

import BusinessLogic.DTOs.CustomerInfo;
import BusinessLogic.DTOs.ItemType;
import BusinessLogic.DTOs.PurchaseDTO;
import BusinessLogic.DTOs.PurchaseItemDTO;
import BusinessLogic.Memberships.ActivationMembershipService;
import BusinessLogic.Purchase.CashPayment;
import BusinessLogic.Purchase.PurchaseService;
import DomainModel.DiscountStrategy.FixedDiscount;
import DomainModel.Membership.*;
import DomainModel.Users.Customer;
import ORM.DailyEvents.TrainerAvailabilityDAO;
import ORM.DiscountStrategy.DiscountsDAO;
import ORM.Membership.*;
import ORM.Users.CustomerDAO;
import ORM.Users.TrainerDAO;
import ORM.Users.UserDAO;
import ORMTest.Users.UserDAOTestUtils;
import TestUtils.DAOTestUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

public class PurchaseTest {
    private PurchaseService purchaseService;
    private ActivationMembershipService activationMembershipService;
    private UserDAO userDAO;
    private CustomerDAO customerDAO;
    private RegistrationFeeDAO registrationFeeDAO;
    private WeightRoomMembershipDAO weightRoomMembershipDAO;
    private CourseDAO courseDAO;
    private CourseMembershipDAO courseMembershipDAO;
    private BundleDAO bundleDAO;
    private DiscountsDAO discountsDAO;
    private CustomerFeeDAO customerFeeDAO;
    private CustomerMembershipDAO customerMembershipDAO;
    private TrainerAvailabilityDAO trainerAvailabilityDAO;

    @BeforeEach
    void setup() {
        DAOTestUtils.resetDatabase();
        this.userDAO = new UserDAO();
        this.customerDAO = new CustomerDAO(userDAO);
        this.registrationFeeDAO = new RegistrationFeeDAO();
        this.courseDAO = new CourseDAO(new TrainerDAO(userDAO));
        this.discountsDAO = new DiscountsDAO();
        MembershipDAO membershipDAO = new MembershipDAO(this.discountsDAO);
        this.weightRoomMembershipDAO = new WeightRoomMembershipDAO(membershipDAO);
        this.courseDAO = new CourseDAO(new TrainerDAO(userDAO));
        this.courseMembershipDAO = new CourseMembershipDAO(membershipDAO, this.courseDAO);
        this.bundleDAO = new BundleDAO(discountsDAO, membershipDAO);
        this.customerFeeDAO = new CustomerFeeDAO(userDAO);
        this.customerMembershipDAO = new CustomerMembershipDAO(courseDAO, membershipDAO);
        this.activationMembershipService = new ActivationMembershipService(customerMembershipDAO, customerFeeDAO, customerDAO, membershipDAO);
        this.purchaseService = new PurchaseService(registrationFeeDAO, activationMembershipService, customerFeeDAO, bundleDAO, customerDAO, membershipDAO);
    }

    @Test
    void MembershipsAndFeeShouldBeActivated() {
        Customer customer = customerDAO.createCustomer(UserDAOTestUtils.createCustomer("customer", "customer@customer.it"));
        RegistrationFee.getRegistrationFee().setPrice(new BigDecimal("30"));
        registrationFeeDAO.createRegistrationFee(RegistrationFee.getRegistrationFee());

        WeightRoomMembership weightRoomMembership = new WeightRoomMembership();
        weightRoomMembership.setName("abbonamento sala pesi");
        weightRoomMembership.setDescription("Descrizione");
        weightRoomMembership.setType(WRMembershipType.BASE);
        weightRoomMembership.setPrice(new BigDecimal(100));
        weightRoomMembership.setDurationInDays(40);
        weightRoomMembership = weightRoomMembershipDAO.createWeightRoomMembership(weightRoomMembership);
        Course course = courseDAO.createCourse(new Course("Yoga", "corso di Yoga"));
        CourseMembership courseMembership = new CourseMembership();
        courseMembership.setName("abbonamento Yoga");
        courseMembership.setDescription("descrizione");
        courseMembership.setCourse(course);
        courseMembership.setWeeklyAccess(2);
        courseMembership.setPrice(new BigDecimal(200));
        courseMembership = courseMembershipDAO.createCourseMembership(courseMembership);

        Bundle bundle = new Bundle();
        bundle.setName("bundle");
        bundle.setDescription("description");
        bundle.addMembership(weightRoomMembership);
        bundle.addMembership(courseMembership);

        FixedDiscount fixedDiscount = new FixedDiscount(50, false, "wr+yoga bundle");
        int id = discountsDAO.createDiscount(fixedDiscount);
        bundle.addDiscount(discountsDAO.getDiscountByID(id));
        bundle = bundleDAO.createBundle(bundle);

        //creation of purchaseDTO
        PurchaseItemDTO purchaseItemDTO = new PurchaseItemDTO(ItemType.BUNDLE,
                bundle.getId(), LocalDate.now());

        ArrayList<PurchaseItemDTO> purchaseItemDTOS = new ArrayList<>();
        purchaseItemDTOS.add(purchaseItemDTO);
        PurchaseDTO purchaseDTO = new PurchaseDTO(purchaseItemDTOS, true, LocalDate.now(), customer.getId());

        assertEquals(new BigDecimal("250.00"), this.purchaseService.calculateTotal(purchaseDTO));
        this.purchaseService.executePurchase(purchaseDTO, new CashPayment());

        //now customer should have two membership activated and registration fee activated
        ArrayList<CustomerMembership> customerMemberships= this.activationMembershipService.getAllCustomerMembership(customer.getId());
        assertEquals(2, customerMemberships.size());
        assertEquals(weightRoomMembership.getId(), customerMemberships.getFirst().getMembership().getId());
        assertEquals(courseMembership.getId(), customerMemberships.get(1).getMembership().getId());

        //registationFee
        CustomerInfo customerInfo = userDAO.getCustomerBookingInfo(customer);
        assertEquals(LocalDate.now(), customerInfo.getFeeBegin());
        assertEquals(LocalDate.now().plusYears(1), customerInfo.getFeeExpiry());
    }

    @AfterAll
    static void teardown() {
        DAOTestUtils.resetDatabase();
    }
}
