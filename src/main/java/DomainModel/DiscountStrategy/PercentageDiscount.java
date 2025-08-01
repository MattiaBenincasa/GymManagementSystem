package DomainModel.DiscountStrategy;

import java.math.BigDecimal;

public class PercentageDiscount implements DiscountStrategy {
    private String description;
    private float percentage;

    public PercentageDiscount(float percentage) {
        this.percentage = percentage;
    }

    @Override
    public BigDecimal applyDiscount(BigDecimal price) {
        BigDecimal discountedPrice = price.multiply(new BigDecimal(1-percentage));
        if (discountedPrice.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Invalid discount: price must be greater than 0 euro.");
        return discountedPrice;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPercentage() {
        return percentage;
    }
}
