package Controllers;

import BusinessLogic.AuthService.AuthService;
import BusinessLogic.AuthService.Session;
import BusinessLogic.Bookings.AppointmentTrainerBookingService;
import BusinessLogic.Bookings.ClassBookingService;
import BusinessLogic.DailyEvents.DailyClassService;
import BusinessLogic.DailyEvents.TrainerAvailabilityService;
import BusinessLogic.DiscountService.DiscountService;
import BusinessLogic.Exceptions.InvalidSessionException;
import BusinessLogic.Exceptions.UnauthorizedException;
import BusinessLogic.Memberships.*;
import BusinessLogic.Purchase.PurchaseService;
import BusinessLogic.Users.CustomerService;
import BusinessLogic.Users.StaffService;
import BusinessLogic.Users.TrainerService;
import BusinessLogic.Users.UserService;
import Controllers.Admin.AdminCourseController;
import Controllers.Admin.AdminMembershipController;
import Controllers.Admin.AdminStaffController;
import Controllers.Customer.CustomerController;
import Controllers.Receptionist.ReceptionistController;
import Controllers.Trainer.TrainerController;
import ORM.Bookings.AppointmentDAO;
import ORM.Bookings.BookingDAO;
import ORM.DailyEvents.DailyClassDAO;
import ORM.DailyEvents.TrainerAvailabilityDAO;
import ORM.DiscountStrategy.DiscountsDAO;
import ORM.Membership.*;
import ORM.Users.CustomerDAO;
import ORM.Users.StaffDAO;
import ORM.Users.TrainerDAO;
import ORM.Users.UserDAO;

import javax.naming.AuthenticationException;
import java.util.Objects;

public class ApplicationManager  {
    private Session currentSession;
    private final AuthService authService;

    // controllers
    private final AdminCourseController adminCourseController;
    private final AdminStaffController adminStaffController;
    private final AdminMembershipController adminMembershipController;
    private final ReceptionistController receptionistController;
    private final TrainerController trainerController;
    private final CustomerController customerController;

    //injects all controller dependencies
    public ApplicationManager () {
        //create all DAOs
        UserDAO userDAO = new UserDAO();
        CustomerDAO customerDAO = new CustomerDAO(userDAO);
        TrainerDAO trainerDAO = new TrainerDAO(userDAO);
        StaffDAO staffDAO = new StaffDAO(userDAO);
        DiscountsDAO discountsDAO = new DiscountsDAO();
        MembershipDAO membershipDAO = new MembershipDAO(discountsDAO);
        CourseDAO courseDAO = new CourseDAO(trainerDAO);
        CourseMembershipDAO courseMembershipDAO = new CourseMembershipDAO(membershipDAO, courseDAO);
        WeightRoomMembershipDAO weightRoomMembershipDAO = new WeightRoomMembershipDAO(membershipDAO);
        BundleDAO bundleDAO = new BundleDAO(discountsDAO, membershipDAO);
        CustomerFeeDAO customerFeeDAO = new CustomerFeeDAO(userDAO);
        CustomerMembershipDAO customerMembershipDAO = new CustomerMembershipDAO(courseDAO, membershipDAO);
        RegistrationFeeDAO registrationFeeDAO = new RegistrationFeeDAO();
        DailyClassDAO dailyClassDAO =  new DailyClassDAO(userDAO);
        TrainerAvailabilityDAO trainerAvailabilityDAO = new TrainerAvailabilityDAO(trainerDAO);
        AppointmentDAO appointmentDAO = new AppointmentDAO(trainerAvailabilityDAO, customerDAO);
        BookingDAO bookingDAO = new BookingDAO(dailyClassDAO, customerDAO);

        //services
        this.authService = new AuthService(userDAO);
        UserService userService = new UserService(userDAO);
        CustomerService customerService = new CustomerService(customerDAO, userDAO);
        StaffService staffService = new StaffService(staffDAO, userDAO);
        TrainerService trainerService = new TrainerService(trainerDAO, userDAO);
        ActivationMembershipService activationMembershipService = new ActivationMembershipService(customerMembershipDAO, customerFeeDAO, customerDAO, membershipDAO);
        BundleService bundleService = new BundleService(bundleDAO, membershipDAO, discountsDAO);
        CourseMembershipService courseMembershipService = new CourseMembershipService(courseDAO, courseMembershipDAO, discountsDAO);
        CourseService courseService = new CourseService(courseDAO, trainerDAO);
        RegistrationFeeService registrationFeeService = new RegistrationFeeService(registrationFeeDAO);
        WRMembershipService wrMembershipService = new WRMembershipService(weightRoomMembershipDAO, discountsDAO);
        PurchaseService purchaseService = new PurchaseService(registrationFeeDAO, activationMembershipService, customerFeeDAO, bundleDAO, customerDAO);
        DiscountService discountService = new DiscountService(discountsDAO);
        DailyClassService dailyClassService = new DailyClassService(dailyClassDAO, courseDAO);
        TrainerAvailabilityService trainerAvailabilityService = new TrainerAvailabilityService(trainerAvailabilityDAO, trainerDAO);
        ClassBookingService classBookingService = new ClassBookingService(bookingDAO, userDAO);
        AppointmentTrainerBookingService appointmentTrainerBookingService = new AppointmentTrainerBookingService(userDAO, appointmentDAO);

        this.adminCourseController = new AdminCourseController(courseService, dailyClassService);
        this.adminMembershipController = new AdminMembershipController(courseMembershipService, wrMembershipService, discountService, staffService, trainerService, bundleService, registrationFeeService);
        this.adminStaffController = new AdminStaffController(staffService, trainerService, userService);
        this.receptionistController = new ReceptionistController(staffService, customerService, purchaseService);
        this.trainerController = new TrainerController(trainerService, trainerAvailabilityService, dailyClassService);
        this.customerController = new CustomerController(activationMembershipService, classBookingService, customerService, appointmentTrainerBookingService);
    }

    public void login(String username, String password) throws AuthenticationException {
        this.currentSession = this.authService.login(username, password);
    }

    public void logout() {
        if (this.currentSession!=null)
            this.authService.logout(this.currentSession);
    }

    public void setCurrentSession(Session session) {
        this.currentSession = session;
    }

    public AdminStaffController getAdminStaffController() {
        if (!this.currentSession.isValid())
            throw new InvalidSessionException("Current session is invalid");

        if(this.currentSession.isValid() && !Objects.equals(this.currentSession.getRole(), "ADMIN"))
            throw new UnauthorizedException("You are not authorized to get this controller");

        return this.adminStaffController;
    }

    public AdminCourseController getAdminCourseController() {
        if (!this.currentSession.isValid())
            throw new InvalidSessionException("Current session is invalid");

        if(this.currentSession.isValid() && !Objects.equals(this.currentSession.getRole(), "ADMIN"))
            throw new UnauthorizedException("You are not authorized to get this controller");

        return this.adminCourseController;
    }

    public AdminMembershipController getAdminMembershipController() {
        if (!this.currentSession.isValid())
            throw new InvalidSessionException("Current session is invalid");

        if(this.currentSession.isValid() && !Objects.equals(this.currentSession.getRole(), "ADMIN"))
            throw new UnauthorizedException("You are not authorized to get this controller");

        return this.adminMembershipController;
    }

    public ReceptionistController getReceptionistController() {
        if (!this.currentSession.isValid())
            throw new InvalidSessionException("Current session is invalid");

        if(this.currentSession.isValid() && !Objects.equals(this.currentSession.getRole(), "RECEPTIONIST"))
            throw new UnauthorizedException("You are not authorized to get this controller");

        return this.receptionistController;
    }

    public TrainerController getTrainerController() {
        if (!this.currentSession.isValid())
            throw new InvalidSessionException("Current session is invalid");

        if(this.currentSession.isValid() && !Objects.equals(this.currentSession.getRole(), "TRAINER"))
            throw new UnauthorizedException("You are not authorized to get this controller");

        return this.trainerController;
    }

    public CustomerController getCustomerController() {
        if (!this.currentSession.isValid())
            throw new InvalidSessionException("Current session is invalid");

        if(this.currentSession.isValid() && !Objects.equals(this.currentSession.getRole(), "CUSTOMER"))
            throw new UnauthorizedException("You are not authorized to get this controller");

        return this.customerController;
    }

}
