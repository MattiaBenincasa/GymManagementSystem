package DomainModel.DiscountStrategy;

import DomainModel.Users.Customer;

import java.math.BigDecimal;

public class FixedDiscount extends DiscountStrategy {
    private int discountInEuro;

    public FixedDiscount(int discountInEuro, boolean isSpecialOffer, String description) {
        super(description, isSpecialOffer);
        this.discountInEuro = discountInEuro;
    }

    public FixedDiscount(int id, String description, boolean isSpecialOffer, int discountInEuro) {
        super(id, description, isSpecialOffer);
        this.discountInEuro = discountInEuro;
    }

    @Override
    public int getValue() {
        return discountInEuro;
    }

    @Override
    public BigDecimal applyDiscount(BigDecimal price, Customer customer) {
        return this.fixedDiscount(price, new BigDecimal(this.discountInEuro));
    }

    public BigDecimal getDiscountInEuro() {
        return new BigDecimal(discountInEuro);
    }

    public void setDiscountInEuro(int discountInEuro) {
        this.discountInEuro = discountInEuro;
    }
}
