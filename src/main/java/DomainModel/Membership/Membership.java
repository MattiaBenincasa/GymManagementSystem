package DomainModel.Membership;

import DomainModel.DiscountStrategy.DiscountStrategy;
import DomainModel.Users.Customer;

import java.math.BigDecimal;

public abstract class Membership extends Purchasable{
    private BigDecimal price;
    private int durationInDays;

    public Membership() {
        super();
    }

    public Membership(int id) {
        super(id);
    }

    public Membership(Membership membership) {
        super(membership);
        this.price = membership.price;
        this.durationInDays = membership.durationInDays;
    }

    public abstract Membership copy();

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getDurationInDays() {
        return durationInDays;
    }

    public void setDurationInDays(int durationInDays) {
        this.durationInDays = durationInDays;
    }

    public BigDecimal getDiscountedPrice(Customer customer) {
        return DiscountStrategy.totalDiscounted(this.price, this.discounts, customer);
    }
}
