package ORMTest.DiscountStrategyTest;

import BusinessLogic.Exceptions.DAOException;
import DomainModel.DiscountStrategy.*;
import DomainModel.Users.CustomerCategory;
import ORM.DiscountStrategy.DiscountsDAO;
import TestUtils.DAOTestUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Month;
import java.util.HashSet;

public class DiscountStrategyTest {
    private PercentageDiscount percentageDiscount;
    private FixedDiscount fixedDiscount;
    private CustomerBasedDiscount customerBasedDiscount;
    private SeasonDiscount seasonDiscount;
    private DiscountsDAO discountsDAO;

    @BeforeEach
    void setup() {
        DAOTestUtils.resetDatabase();
        this.percentageDiscount = new PercentageDiscount(10, false, "percentage discount");
        this.fixedDiscount = new FixedDiscount(10, true, "fixed discount");
        this.customerBasedDiscount = new CustomerBasedDiscount(CustomerCategory.STUDENT, 10, "Student discount");
        HashSet<Month> monthsWithDiscount = new HashSet<>();
        monthsWithDiscount.add(Month.JANUARY);
        monthsWithDiscount.add(Month.FEBRUARY);
        this.seasonDiscount = new SeasonDiscount(monthsWithDiscount, 15, "Winter discount", Clock.systemDefaultZone());
        this.discountsDAO = new DiscountsDAO();
    }

    @Test
    void testCreateDiscount() {
        int idPercentage = this.discountsDAO.createDiscount(this.percentageDiscount);
        int idFixed = this.discountsDAO.createDiscount(this.fixedDiscount);
        int idCustomerBased = this.discountsDAO.createDiscount(this.customerBasedDiscount);
        int idSeasonal = this.discountsDAO.createDiscount(this.seasonDiscount);

        PercentageDiscount percentageDiscountRetrieved = (PercentageDiscount) this.discountsDAO.getDiscountByID(idPercentage);
        Assertions.assertEquals(this.percentageDiscount.getPercentage(), percentageDiscountRetrieved.getPercentage());
        Assertions.assertEquals(this.percentageDiscount.getDescription(), percentageDiscountRetrieved.getDescription());
        Assertions.assertEquals(this.percentageDiscount.isSpecialOffer(), percentageDiscountRetrieved.isSpecialOffer());

        FixedDiscount fixedDiscountRetrieved = (FixedDiscount) this.discountsDAO.getDiscountByID(idFixed);
        Assertions.assertEquals(this.fixedDiscount.getDiscountInEuro(), fixedDiscountRetrieved.getDiscountInEuro());
        Assertions.assertEquals(this.fixedDiscount.getDescription(), fixedDiscountRetrieved.getDescription());
        Assertions.assertEquals(this.fixedDiscount.isSpecialOffer(), fixedDiscountRetrieved.isSpecialOffer());

        CustomerBasedDiscount customerBasedDiscountRetrieved = (CustomerBasedDiscount) this.discountsDAO.getDiscountByID(idCustomerBased);
        Assertions.assertEquals(this.customerBasedDiscount.getPercentage(), customerBasedDiscountRetrieved.getPercentage());
        Assertions.assertEquals(this.customerBasedDiscount.getDescription(), customerBasedDiscountRetrieved.getDescription());
        Assertions.assertEquals(this.customerBasedDiscount.isSpecialOffer(), fixedDiscountRetrieved.isSpecialOffer());
        Assertions.assertEquals(this.customerBasedDiscount.getCustomerCategory(), customerBasedDiscountRetrieved.getCustomerCategory());

        SeasonDiscount seasonDiscountRetrieved = (SeasonDiscount) this.discountsDAO.getDiscountByID(idSeasonal);
        Assertions.assertEquals(this.seasonDiscount.getPercentage(), seasonDiscountRetrieved.getPercentage());
        Assertions.assertEquals(this.seasonDiscount.getDescription(), seasonDiscountRetrieved.getDescription());
        Assertions.assertEquals(this.seasonDiscount.isSpecialOffer(), seasonDiscountRetrieved.isSpecialOffer());
        Assertions.assertEquals(this.seasonDiscount.getMonthsWithDiscount(), seasonDiscountRetrieved.getMonthsWithDiscount());
    }

    @Test
    void testUpdateDiscount() {
        //add discounts to db
        int idPercentage = this.discountsDAO.createDiscount(this.percentageDiscount);
        int idFixed = this.discountsDAO.createDiscount(this.fixedDiscount);
        int idCustomerBased = this.discountsDAO.createDiscount(this.customerBasedDiscount);
        int idSeasonal = this.discountsDAO.createDiscount(this.seasonDiscount);

        //get discounts with ids
        this.percentageDiscount = (PercentageDiscount) this.discountsDAO.getDiscountByID(idPercentage);
        this.fixedDiscount = (FixedDiscount) this.discountsDAO.getDiscountByID(idFixed);
        this.customerBasedDiscount = (CustomerBasedDiscount) this.discountsDAO.getDiscountByID(idCustomerBased);
        this.seasonDiscount = (SeasonDiscount) this.discountsDAO.getDiscountByID(idSeasonal);


        //update some discounts
        this.percentageDiscount.setDescription("updated description");
        this.fixedDiscount.setDiscountInEuro(30);
        this.customerBasedDiscount.setCustomerCategory(CustomerCategory.MILITARY);
        HashSet<Month> newMonthWithDiscount = new HashSet<>();
        newMonthWithDiscount.add(Month.JUNE);
        newMonthWithDiscount.add(Month.JULY);
        this.seasonDiscount.setMonthsWithDiscount(newMonthWithDiscount);

        //update and retrieve
        this.discountsDAO.updateDiscount(this.percentageDiscount);
        this.discountsDAO.updateDiscount(this.fixedDiscount);
        this.discountsDAO.updateDiscount(this.customerBasedDiscount);
        this.discountsDAO.updateDiscount(this.seasonDiscount);

        //check if the discounts have been updated
        PercentageDiscount percentageDiscountRetrieved = (PercentageDiscount) this.discountsDAO.getDiscountByID(idPercentage);
        Assertions.assertEquals("updated description", percentageDiscountRetrieved.getDescription());

        FixedDiscount fixedDiscountRetrieved = (FixedDiscount) this.discountsDAO.getDiscountByID(idFixed);
        Assertions.assertEquals(new BigDecimal("30"), fixedDiscountRetrieved.getDiscountInEuro());

        CustomerBasedDiscount customerBasedDiscountRetrieved = (CustomerBasedDiscount) this.discountsDAO.getDiscountByID(idCustomerBased);
        Assertions.assertEquals(CustomerCategory.MILITARY, customerBasedDiscountRetrieved.getCustomerCategory());

        SeasonDiscount seasonDiscountRetrieved = (SeasonDiscount) this.discountsDAO.getDiscountByID(idSeasonal);
        Assertions.assertEquals(newMonthWithDiscount, seasonDiscountRetrieved.getMonthsWithDiscount());
    }

    @Test
    void deleteDiscount() {
        //add discounts to db
        int idPercentage = this.discountsDAO.createDiscount(this.percentageDiscount);
        int idFixed = this.discountsDAO.createDiscount(this.fixedDiscount);
        int idCustomerBased = this.discountsDAO.createDiscount(this.customerBasedDiscount);
        int idSeasonal = this.discountsDAO.createDiscount(this.seasonDiscount);

        this.discountsDAO.deleteDiscount(idPercentage);
        this.discountsDAO.deleteDiscount(idFixed);
        this.discountsDAO.deleteDiscount(idCustomerBased);
        this.discountsDAO.deleteDiscount(idSeasonal);

        Assertions.assertThrows(DAOException.class, ()->{
            this.discountsDAO.getDiscountByID(idPercentage);
            this.discountsDAO.getDiscountByID(idFixed);
            this.discountsDAO.getDiscountByID(idCustomerBased);
            this.discountsDAO.getDiscountByID(idSeasonal);
        });

    }

    @AfterAll
    static void teardown() {
        DAOTestUtils.resetDatabase();
    }

}
