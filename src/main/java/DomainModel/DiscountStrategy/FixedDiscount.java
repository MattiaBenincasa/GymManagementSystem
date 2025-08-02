package DomainModel.DiscountStrategy;

import DomainModel.Users.Customer;

import java.math.BigDecimal;

public class FixedDiscount extends DiscountStrategy {
    private BigDecimal discountInEuro;

    public FixedDiscount(BigDecimal discountInEuro, boolean isSpecialOffer, String description) {
        super(description, isSpecialOffer);
        this.discountInEuro = discountInEuro;
    }

    @Override
    public BigDecimal applyDiscount(BigDecimal price, Customer customer) {
        return this.fixedDiscount(price, this.discountInEuro);
    }

    public BigDecimal getDiscountInEuro() {
        return discountInEuro;
    }

    public void setDiscountInEuro(BigDecimal discountInEuro) {
        this.discountInEuro = discountInEuro;
    }
}
