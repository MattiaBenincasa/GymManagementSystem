package DomainModel.DiscountStrategy;

import DomainModel.Users.Customer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;

public class SeasonDiscount extends DiscountStrategy {
    private HashSet<Month> monthsWithDiscount;
    private int percentage;

    public SeasonDiscount(HashSet<Month> monthsWithDiscount, int percentage, String description) {
        super(description, true);
        this.monthsWithDiscount = monthsWithDiscount;
        this.percentage = percentage;
    }

    @Override
    BigDecimal applyDiscount(BigDecimal price, Customer customer) {
        BigDecimal discountedPrice = price;
        if (this.monthsWithDiscount.contains(LocalDate.now().getMonth()))
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
