package DomainModel.DiscountStrategy;

import java.math.BigDecimal;

public class FixedDiscount implements DiscountStrategy {
    private String description;
    private BigDecimal discountInEuro;

    public FixedDiscount(BigDecimal discountInEuro) {
        this.discountInEuro = discountInEuro;
    }

    @Override
    public BigDecimal applyDiscount(BigDecimal price) {
        BigDecimal discountedPrice = price.subtract(this.discountInEuro);
        if (discountedPrice.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Invalid discount: price must be greater than 0 euro.");
        return discountedPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getDiscountInEuro() {
        return discountInEuro;
    }

    public void setDiscountInEuro(BigDecimal discountInEuro) {
        this.discountInEuro = discountInEuro;
    }
}
