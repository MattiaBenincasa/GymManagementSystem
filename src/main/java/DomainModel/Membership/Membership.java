package DomainModel.Membership;

import DomainModel.DiscountStrategy.DiscountStrategy;
import DomainModel.Users.Customer;

import java.math.BigDecimal;
import java.util.ArrayList;

public abstract class Membership {
    private int id;
    private String name;
    private BigDecimal price;
    private String description;
    private int durationInDays;
    private ArrayList<DiscountStrategy> discounts;

    public Membership() {}

    public Membership(int id) {
        this.id = id;
        this.discounts = new ArrayList<>();
    }

    public Membership(Membership membership) {
        this.id = membership.id;
        this.name = membership.name;
        this.price = membership.price;
        this.description = membership.description;
        this.durationInDays = membership.durationInDays;
        this.discounts = membership.discounts;
    }

    public abstract Membership copy();

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDurationInDays() {
        return durationInDays;
    }

    public void setDurationInDays(int durationInDays) {
        this.durationInDays = durationInDays;
    }

    public void addDiscount(DiscountStrategy discountStrategy) {
        this.discounts.add(discountStrategy);
    }

    public void remove(DiscountStrategy discountStrategy) {
        this.discounts.remove(discountStrategy);
    }

    public BigDecimal getDiscountedPrice(Customer customer) {
        return DiscountStrategy.totalDiscounted(this.price, this.discounts, customer);
    }
}
