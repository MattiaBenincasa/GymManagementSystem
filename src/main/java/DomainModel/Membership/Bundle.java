package DomainModel.Membership;

import DomainModel.DiscountStrategy.DiscountStrategy;
import DomainModel.Users.Customer;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Bundle {
    private int id;
    private String name;
    private String description;
    private final ArrayList<DiscountStrategy> discounts;
    private final ArrayList<Membership> memberships;

    public Bundle() {
        this.memberships = new ArrayList<>();
        this.discounts = new ArrayList<>();
    }

    public Bundle(int id) {
        this.id = id;
        this.memberships = new ArrayList<>();
        this.discounts = new ArrayList<>();
    }

    public void addMembership(Membership membership) {
        this.memberships.add(membership);
    }

    public void remove(Membership membership) {
        this.memberships.remove(membership);
    }

    public BigDecimal getPrice() {
        BigDecimal total = BigDecimal.ZERO;
        for (Membership membership : this.memberships)
            total = total.add(membership.getPrice());

        return total;
    }

    public BigDecimal getDiscountedPrice(Customer customer) {
        BigDecimal total = getPrice();
        return DiscountStrategy.totalDiscounted(total, this.discounts, customer);
    }

    public ArrayList<Membership> getMemberships() {
        return new ArrayList<>(this.memberships);
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
