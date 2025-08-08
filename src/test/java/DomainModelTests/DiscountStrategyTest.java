package DomainModelTests;

import DomainModel.DiscountStrategy.*;
import DomainModel.Membership.Bundle;
import DomainModel.Membership.Membership;
import DomainModel.Membership.WeightRoomMembership;
import DomainModel.Users.Customer;
import DomainModel.Users.CustomerCategory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;

public class DiscountStrategyTest {
    Membership membership;

    @BeforeEach
    public void Setup() {
        this.membership = new WeightRoomMembership();
        this.membership.setPrice(new BigDecimal("350.00"));
    }

    @Test
    public void testPercentageDiscount() {
        PercentageDiscount percentageDiscount = new PercentageDiscount(10,
                true,
                "ten percent discount");

        this.membership.addDiscount(percentageDiscount);
        Assertions.assertEquals(new BigDecimal("315.00"), this.membership.getDiscountedPrice(null));

    }

    @Test
    public void testFixedDiscount() {
        FixedDiscount fixedDiscount = new FixedDiscount(30,
                true,
                "30 euro discount");

        this.membership.addDiscount(fixedDiscount);
        Assertions.assertEquals(new BigDecimal("320.00"), this.membership.getDiscountedPrice(null));
    }

    @Test
    public void testSeasonDiscount() {
        HashSet<Month> winterMonthsWithDiscount = new HashSet<>();
        winterMonthsWithDiscount.add(Month.SEPTEMBER);
        winterMonthsWithDiscount.add(Month.JANUARY);

        HashSet<Month> summerMonthsWithDiscount = new HashSet<>();
        summerMonthsWithDiscount.add(Month.JUNE);
        summerMonthsWithDiscount.add(Month.JULY);
        Clock fixedClock = Clock.fixed(
                LocalDate.of(2025, 1, 15).atStartOfDay(ZoneId.systemDefault()).toInstant(),
                ZoneId.systemDefault());
        SeasonDiscount winterSeasonDiscount = new SeasonDiscount(winterMonthsWithDiscount, 10, "Winter season discounts", fixedClock);
        SeasonDiscount summerSeasonDiscount = new SeasonDiscount(summerMonthsWithDiscount, 10, "Summer season discounts", fixedClock);

        this.membership.addDiscount(winterSeasonDiscount);
        //discount applied
        Assertions.assertEquals(new BigDecimal("315.00"), this.membership.getDiscountedPrice(null));
        this.membership.removeDiscount(winterSeasonDiscount);
        this.membership.addDiscount(summerSeasonDiscount);
        //discount not applied
        Assertions.assertEquals(new BigDecimal("350.00"), this.membership.getDiscountedPrice(null));

    }

    @Test
    public void testCustomerBasedDiscount() {
        Customer militaryCustomer = new Customer();
        Customer studentCustomer = new Customer();

        militaryCustomer.setCustomerCategory(CustomerCategory.MILITARY);
        studentCustomer.setBirthDate(LocalDate.of(2003, 2, 18));
        studentCustomer.setCustomerCategory(CustomerCategory.STUDENT);
        CustomerBasedDiscount studentDiscount = new CustomerBasedDiscount(CustomerCategory.STUDENT, 20, "discount for students");
        CustomerBasedDiscount militaryPersonnelDiscount = new CustomerBasedDiscount(CustomerCategory.MILITARY, 10, "discount for military personnel");
        this.membership.addDiscount(militaryPersonnelDiscount);
        this.membership.addDiscount(studentDiscount);
        Assertions.assertEquals(new BigDecimal("315.00"), this.membership.getDiscountedPrice(militaryCustomer));
        Assertions.assertEquals(new BigDecimal("280.00"), this.membership.getDiscountedPrice(studentCustomer));
    }

    @Test
    public void testMultipleDiscount() {
        /*
        * a student wants to buy a membership with both seasonal discount
        * and student discount in a winter month. Only the best discount
        * should be applied because they are both special offers.
        * */

        Customer student = new Customer();
        student.setBirthDate(LocalDate.of(2003, 2, 18));
        student.setCustomerCategory(CustomerCategory.STUDENT);

        HashSet<Month> winterMonthsWithDiscount = new HashSet<>();
        winterMonthsWithDiscount.add(Month.SEPTEMBER);
        winterMonthsWithDiscount.add(Month.JANUARY);

        Clock fixedClock = Clock.fixed(
                LocalDate.of(2025, 1, 15).atStartOfDay(ZoneId.systemDefault()).toInstant(),
                ZoneId.systemDefault());
        SeasonDiscount winterSeasonDiscount = new SeasonDiscount(winterMonthsWithDiscount, 10, "Winter season discounts", fixedClock);
        CustomerBasedDiscount studentDiscount = new CustomerBasedDiscount(CustomerCategory.STUDENT, 20, "discount for students");

        this.membership.addDiscount(winterSeasonDiscount);
        this.membership.addDiscount(studentDiscount);

        Assertions.assertEquals(new BigDecimal("280.00"), this.membership.getDiscountedPrice(student));

    }
    
    @Test
    public void testMultipleDiscountsOnBundle() {
        Bundle bundle = getBundle();

        Assertions.assertEquals(new BigDecimal("450.00"), bundle.getPrice());


        HashSet<Month> winterMonthsWithDiscount = new HashSet<>();
        winterMonthsWithDiscount.add(Month.SEPTEMBER);
        winterMonthsWithDiscount.add(Month.JANUARY);
        Clock fixedClock = Clock.fixed(
                LocalDate.of(2025, 1, 15).atStartOfDay(ZoneId.systemDefault()).toInstant(),
                ZoneId.systemDefault());
        //discount related to the bundle
        FixedDiscount fixedDiscount = new FixedDiscount(50, false, "bundle discount");
        //special offers related to the bundle
        CustomerBasedDiscount studentDiscount = new CustomerBasedDiscount(CustomerCategory.STUDENT, 20, "student discount");
        SeasonDiscount winterDiscount = new SeasonDiscount(winterMonthsWithDiscount, 10, "winter season discount", fixedClock);
        bundle.addDiscount(fixedDiscount);
        bundle.addDiscount(studentDiscount);
        bundle.addDiscount(winterDiscount);
        Customer normalCustomer = new Customer();
        Customer student = new Customer();
        student.setBirthDate(LocalDate.of(2003, 2, 18));
        student.setCustomerCategory(CustomerCategory.STUDENT);
        //normal customer buy the bundle during winter period:
        // bundle discount + winter discount should be applied
        Assertions.assertEquals(new BigDecimal("360.00"), bundle.getDiscountedPrice(normalCustomer));

        //student customer buy the bundle during winter period:
        //bundle discount + student discount should be applied
        Assertions.assertEquals(new BigDecimal("320.00"), bundle.getDiscountedPrice(student));

    }

    private static Bundle getBundle() {
        Membership membership1 = new WeightRoomMembership();
        Membership membership2 = new WeightRoomMembership();
        Membership membership3 = new WeightRoomMembership();

        membership1.setPrice(new BigDecimal("150.00"));
        membership2.setPrice(new BigDecimal("200.00"));
        membership3.setPrice(new BigDecimal("100.00"));

        Bundle bundle = new Bundle();
        bundle.addMembership(membership1);
        bundle.addMembership(membership2);
        bundle.addMembership(membership3);
        return bundle;
    }

}
