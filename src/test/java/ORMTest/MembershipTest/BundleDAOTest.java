package ORMTest.MembershipTest;

import Exceptions.DAOException;
import DomainModel.DiscountStrategy.FixedDiscount;
import DomainModel.DiscountStrategy.PercentageDiscount;
import DomainModel.Membership.Bundle;
import DomainModel.Membership.WRMembershipType;
import DomainModel.Membership.WeightRoomMembership;
import ORM.DiscountStrategy.DiscountsDAO;
import ORM.Membership.BundleDAO;
import ORM.Membership.MembershipDAO;
import ORM.Membership.WeightRoomMembershipDAO;
import TestUtils.DAOTestUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class BundleDAOTest {
    private BundleDAO bundleDAO;
    private Bundle bundle;
    private DiscountsDAO discountsDAO;
    private WeightRoomMembershipDAO weightRoomMembershipDAO;


    @BeforeEach
    void setup() {
        DAOTestUtils.resetDatabase();
        this.discountsDAO = new DiscountsDAO();
        MembershipDAO membershipDAO = new MembershipDAO(this.discountsDAO);
        this.bundleDAO = new BundleDAO(discountsDAO, membershipDAO);
        this.weightRoomMembershipDAO = new WeightRoomMembershipDAO(membershipDAO);

        // bundle creation
        this.bundle = new Bundle();
        this.bundle.setName("Bundle");
        this.bundle.setDescription("Bundle della sala pesi");
        //add membership
        WeightRoomMembership membership1 = new WeightRoomMembership();
        membership1.setName("abbonamento");
        membership1.setDescription("descrizione 1");
        membership1.setPrice(new BigDecimal("500.00"));
        membership1.setDurationInDays(30);
        membership1.setType(WRMembershipType.BASE);
        WeightRoomMembership membership2 = new WeightRoomMembership();
        membership2.setName("abbonamento2");
        membership2.setDescription("descrizione 2");
        membership2.setPrice(new BigDecimal("600.00"));
        membership2.setDurationInDays(30);
        membership2.setType(WRMembershipType.PERSONAL);
        membership1 = weightRoomMembershipDAO.createWeightRoomMembership(membership1);
        membership2 = weightRoomMembershipDAO.createWeightRoomMembership(membership2);
        this.bundle.addMembership(membership1);
        this.bundle.addMembership(membership2);

        //add discount
        FixedDiscount fixedDiscount = new FixedDiscount(30, false, "sconto bundle");
        int discountID = this.discountsDAO.createDiscount(fixedDiscount);
        this.bundle.addDiscount(this.discountsDAO.getDiscountByID(discountID));
    }

    @Test
    void testCreateBundle() {
        this.bundle = this.bundleDAO.createBundle(this.bundle);
        Bundle retrievedBundle = this.bundleDAO.getBundleByID(this.bundle.getId());

        assertEquals(this.bundle.getName(), retrievedBundle.getName());
        assertEquals(this.bundle.getDescription(), retrievedBundle.getDescription());
        assertEquals(2, retrievedBundle.getMemberships().size());
        assertEquals(this.bundle.getDiscountedPrice(null), retrievedBundle.getDiscountedPrice(null));
    }

    @Test
    void testUpdateBundle() {
        this.bundle = this.bundleDAO.createBundle(this.bundle);

        // add new course to bundle
        WeightRoomMembership membership3 = new WeightRoomMembership();
        membership3.setName("abbonamento3");
        membership3.setDescription("descrizione 3");
        membership3.setPrice(new BigDecimal("200.00"));
        membership3.setDurationInDays(10);
        membership3.setType(WRMembershipType.BASE);
        membership3 = weightRoomMembershipDAO.createWeightRoomMembership(membership3);
        this.bundle.addMembership(membership3);

        //new discount
        PercentageDiscount percentageDiscount = new PercentageDiscount(5, true, "5 percent discount");
        int discountID = this.discountsDAO.createDiscount(percentageDiscount);
        this.bundle.addDiscount(this.discountsDAO.getDiscountByID(discountID));

        this.bundle.setName("New name");
        this.bundle.setDescription("new description");
        this.bundleDAO.updateBundle(this.bundle);
        Bundle retrievedBundle = this.bundleDAO.getBundleByID(this.bundle.getId());
        assertEquals("New name", retrievedBundle.getName());
        assertEquals("new description", retrievedBundle.getDescription());
        assertEquals(3, retrievedBundle.getMemberships().size());
        assertEquals(this.bundle.getDiscountedPrice(null), retrievedBundle.getDiscountedPrice(null));
    }

    @Test
    void testDeleteBundle() {
        this.bundle = this.bundleDAO.createBundle(this.bundle);
        this.bundleDAO.deleteBundle(this.bundle.getId());

        assertThrows(DAOException.class, ()->{
            this.bundleDAO.getBundleByID(this.bundle.getId());
        });
    }

    @AfterAll
    static void tearDown() {
        DAOTestUtils.resetDatabase();
    }
}
