package ORMTest.MembershipTest;

import Exceptions.DAOException;
import DomainModel.DiscountStrategy.FixedDiscount;
import DomainModel.DiscountStrategy.PercentageDiscount;
import DomainModel.Membership.WRMembershipType;
import DomainModel.Membership.WeightRoomMembership;
import ORM.DiscountStrategy.DiscountsDAO;
import ORM.Membership.MembershipDAO;
import ORM.Membership.WeightRoomMembershipDAO;
import TestUtils.DAOTestUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class WeightRoomMembershipDAOTest {
    private WeightRoomMembershipDAO wrMembershipDAO;
    private WeightRoomMembership weightRoomMembership;
    private DiscountsDAO discountsDAO;

    @BeforeEach
    void setUp() {
        DAOTestUtils.resetDatabase();
        this.discountsDAO = new DiscountsDAO();
        wrMembershipDAO = new WeightRoomMembershipDAO(new MembershipDAO(this.discountsDAO));
        this.weightRoomMembership = new WeightRoomMembership();
        this.weightRoomMembership.setName("Abbonamento");
        this.weightRoomMembership.setDescription("Abbonamento per sala pesi");
        this.weightRoomMembership.setPrice(new BigDecimal("99.99"));
        this.weightRoomMembership.setDurationInDays(90);
        this.weightRoomMembership.setType(WRMembershipType.BASE);
    }

    @Test
    void testCreateWRMembership() throws DAOException {

        assertDoesNotThrow(()->{
            WeightRoomMembership created = wrMembershipDAO.createWeightRoomMembership(this.weightRoomMembership);
            wrMembershipDAO.getWeightRoomMembershipByID(created.getId());
        });
    }

    @Test
    void testUpdateWRMembership() throws DAOException {
        this.weightRoomMembership = this.wrMembershipDAO.createWeightRoomMembership(this.weightRoomMembership);

        this.weightRoomMembership = wrMembershipDAO.getWeightRoomMembershipByID(this.weightRoomMembership.getId());
        this.weightRoomMembership.setName("WRUpdated");
        this.weightRoomMembership.setDescription("Descrizione Updated");
        this.weightRoomMembership.setDurationInDays(90);
        this.weightRoomMembership.setPrice(new BigDecimal("120.00"));
        this.weightRoomMembership.setType(WRMembershipType.PERSONAL);

        wrMembershipDAO.updateWeightRoomMembership(this.weightRoomMembership);

        WeightRoomMembership retrieved = wrMembershipDAO.getWeightRoomMembershipByID(this.weightRoomMembership.getId());
        assertEquals("WRUpdated", retrieved.getName());
        assertEquals(WRMembershipType.PERSONAL, retrieved.getType());
        assertEquals("Descrizione Updated", retrieved.getDescription());
        assertEquals(90, retrieved.getDurationInDays());
        assertEquals(new BigDecimal("120.00"), retrieved.getPrice());

        //creating discounts and adding them to membership
        PercentageDiscount percentageDiscount = new PercentageDiscount(10,
                true,
                "percentage discount");

        FixedDiscount fixedDiscount = new FixedDiscount(30,
                false,
                "fixed discount");
        int idPercentage = this.discountsDAO.createDiscount(percentageDiscount);
        int idFixed = this.discountsDAO.createDiscount(fixedDiscount);
        this.weightRoomMembership.addDiscount(this.discountsDAO.getDiscountByID(idPercentage));
        this.weightRoomMembership.addDiscount(this.discountsDAO.getDiscountByID(idFixed));

        this.wrMembershipDAO.updateWeightRoomMembership(this.weightRoomMembership);
        retrieved = this.wrMembershipDAO.getWeightRoomMembershipByID(this.weightRoomMembership.getId());
        assertEquals(this.weightRoomMembership.getDiscounts().getFirst().getId(),
                retrieved.getDiscounts().getFirst().getId());

        assertEquals(this.weightRoomMembership.getDiscounts().get(1).getId(),
                retrieved.getDiscounts().get(1).getId());
    }

    @Test
    void testDeleteWRMembership() {
        this.weightRoomMembership = this.wrMembershipDAO.createWeightRoomMembership(this.weightRoomMembership);
        wrMembershipDAO.deleteWRMembership(this.weightRoomMembership.getId());

        assertThrows(DAOException.class, () -> {
            wrMembershipDAO.getWeightRoomMembershipByID(this.weightRoomMembership.getId());
        });
    }

    @AfterAll
    static void tearDown() {
        DAOTestUtils.resetDatabase();
    }
}