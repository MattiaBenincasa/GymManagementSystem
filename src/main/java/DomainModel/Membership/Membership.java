package DomainModel.Membership;

import DomainModel.DiscountStrategy.DiscountStrategy;
import DomainModel.Users.Customer;

import java.math.BigDecimal;
import java.util.ArrayList;

public abstract class Membership {
    protected int id;
    protected String name;
    protected String description;
    private final ArrayList<DiscountStrategy> discounts;
    private BigDecimal price;
    protected int durationInDays;

    public Membership() {
        this.discounts = new ArrayList<>();
    }

    public Membership(int id) {
        this.discounts = new ArrayList<>();
        this.id = id;
    }

    public Membership(Membership membership) {
        this.id = membership.id;
        this.name = membership.name;
        this.description = membership.description;
        this.discounts = new ArrayList<>(membership.discounts);
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

    public void removeDiscount(DiscountStrategy discountStrategy) {
        this.discounts.remove(discountStrategy);
    }

    public void addDiscount(DiscountStrategy discountStrategy) {
        this.discounts.add(discountStrategy);
    }

    public ArrayList<DiscountStrategy> getDiscounts() {
        return this.discounts;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
