package DomainModel.DiscountStrategy;

import DomainModel.Users.Customer;

import java.math.BigDecimal;

public class PercentageDiscount extends DiscountStrategy {
    private int percentage;

    public PercentageDiscount(int percentage, boolean isSpecialOffer, String description) {
        super(description, isSpecialOffer);
        this.percentage = percentage;
    }

    @Override
    public BigDecimal applyDiscount(BigDecimal price, Customer customer) {
        return this.percentageDiscount(price, this.percentage);
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public int getPercentage() {
        return percentage;
    }
}
