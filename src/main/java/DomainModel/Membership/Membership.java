package DomainModel.Membership;

import DomainModel.DiscountStrategy.DiscountStrategy;

import java.math.BigDecimal;

public abstract class Membership {
    private int id;
    private String name;
    private BigDecimal price;
    private String description;
    private int durationInDays;
    private DiscountStrategy discountStrategy;

    public Membership() {}

    public Membership(int id) {
        this.id = id;
    }

    public Membership(Membership membership) {
        this.id = membership.id;
        this.name = membership.name;
        this.price = membership.price;
        this.description = membership.description;
        this.durationInDays = membership.durationInDays;
        this.discountStrategy = membership.discountStrategy;
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

    public DiscountStrategy getDiscountStrategy() {
        return discountStrategy;
    }

    public void setDiscountStrategy(DiscountStrategy discountStrategy) {
        this.discountStrategy = discountStrategy;
    }

    public BigDecimal getDiscountedPrice() {
        BigDecimal discountedPrice = this.price;
        if (this.discountStrategy != null)
            discountedPrice = this.discountStrategy.applyDiscount(discountedPrice);
        return discountedPrice;
    }
}
