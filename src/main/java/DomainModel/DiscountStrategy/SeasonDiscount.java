package DomainModel.DiscountStrategy;

import DomainModel.Users.Customer;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;

public class SeasonDiscount extends DiscountStrategy {
    private HashSet<Month> monthsWithDiscount;
    private int percentage;
    private Clock clock;

    public SeasonDiscount(HashSet<Month> monthsWithDiscount, int percentage, String description, Clock clock) {
        super(description, true);
        this.monthsWithDiscount = monthsWithDiscount;
        this.percentage = percentage;
        this.clock = clock;
    }

    public SeasonDiscount(int id, String description, boolean isSpecialOffer, HashSet<Month> monthsWithDiscount, int percentage) {
        super(id, description, isSpecialOffer);
        this.monthsWithDiscount = monthsWithDiscount;
        this.percentage = percentage;
        this.clock = Clock.systemDefaultZone();
    }

    @Override
    public int getValue() {
        return percentage;
    }

    @Override
    BigDecimal applyDiscount(BigDecimal price, Customer customer) {
        BigDecimal discountedPrice = price;
        if (this.monthsWithDiscount.contains(LocalDate.now(this.clock).getMonth()))
            discountedPrice = this.percentageDiscount(price, percentage);
        return discountedPrice;
    }

    public HashSet<Month> getMonthsWithDiscount() {
        return monthsWithDiscount;
    }

    public void setMonthsWithDiscount(HashSet<Month> monthsWithDiscount) {
        this.monthsWithDiscount = monthsWithDiscount;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }
}
