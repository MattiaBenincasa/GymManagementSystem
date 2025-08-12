package BusinessLogic.DiscountService;

import DomainModel.DiscountStrategy.*;
import DomainModel.Users.CustomerCategory;
import ORM.DiscountStrategy.DiscountsDAO;

import java.time.Clock;
import java.time.Month;
import java.util.HashSet;

public class DiscountService {
    private final DiscountsDAO discountsDAO;

    public DiscountService(DiscountsDAO discountsDAO) {
        this.discountsDAO = discountsDAO;
    }

    public int createPercentageDiscount(String description, boolean isSpecialOffer, int percentage) {
        PercentageDiscount percentageDiscount = new PercentageDiscount(percentage, isSpecialOffer, description);
        return this.discountsDAO.createDiscount(percentageDiscount);
    }

    public int createFixedDiscount(String description, boolean isSpecialOffer, int discountInEuro) {
        FixedDiscount fixedDiscount = new FixedDiscount(discountInEuro, isSpecialOffer, description);
        return this.discountsDAO.createDiscount(fixedDiscount);
    }

    public int createCustomerBasedDiscount(CustomerCategory category, int percentage, String description) {
        CustomerBasedDiscount customerBasedDiscount = new CustomerBasedDiscount(category, percentage, description);
        return this.discountsDAO.createDiscount(customerBasedDiscount);
    }

    public int createSeasonDiscount(HashSet<Month> months, int percentage, String description) {
        SeasonDiscount seasonDiscount = new SeasonDiscount(months, percentage, description, Clock.systemDefaultZone());
        return this.discountsDAO.createDiscount(seasonDiscount);
    }

    public void deleteDiscount(int discountID) {
        this.discountsDAO.deleteDiscount(discountID);
    }

    public void updateDiscount(DiscountStrategy updatedDiscountStrategy) {
        this.discountsDAO.updateDiscount(updatedDiscountStrategy);
    }

    public PercentageDiscount getPercentageDiscount(int discountID) {
        DiscountStrategy discountStrategy = this.discountsDAO.getDiscountByID(discountID);
        if (discountStrategy instanceof PercentageDiscount percentageDiscount)
            return percentageDiscount;
        else throw new IllegalArgumentException("Wrong id for this kind of discount");
    }

    public FixedDiscount getFixedDiscount(int discountID) {
        DiscountStrategy discountStrategy = this.discountsDAO.getDiscountByID(discountID);
        if (discountStrategy instanceof FixedDiscount fixedDiscount)
            return fixedDiscount;
        else throw new IllegalArgumentException("Wrong id for this kind of discount");
    }

    public CustomerBasedDiscount getCustomerBasedDiscount(int discountID) {
        DiscountStrategy discountStrategy = this.discountsDAO.getDiscountByID(discountID);
        if (discountStrategy instanceof CustomerBasedDiscount customerBasedDiscount)
            return customerBasedDiscount;
        else throw new IllegalArgumentException("Wrong id for this kind of discount");
    }

    public SeasonDiscount seasonDiscount(int discountID) {
        DiscountStrategy discountStrategy = this.discountsDAO.getDiscountByID(discountID);
        if (discountStrategy instanceof SeasonDiscount seasonDiscount)
            return seasonDiscount;
        else throw new IllegalArgumentException("Wrong id for this kind of discount");
    }

}
