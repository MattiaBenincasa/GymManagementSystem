package DomainModel.Membership;

import DomainModel.DiscountStrategy.DiscountStrategy;
import DomainModel.Users.Customer;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Bundle {
    private String name;
    private String description;
    private final ArrayList<Membership> memberships;
    private final ArrayList<DiscountStrategy> discounts;

    public Bundle() {
        this.memberships = new ArrayList<>();
        this.discounts = new ArrayList<>();
    }

    public void addDiscount(DiscountStrategy discountStrategy) {
        this.discounts.add(discountStrategy);
    }

    public void removeDiscount(DiscountStrategy discountStrategy) {
        this.discounts.remove(discountStrategy);
    }

    public void addMembership(Membership membership) {
        this.memberships.add(membership);
    }

    public void remove(Membership membership) {
        this.memberships.remove(membership);
    }

    public BigDecimal calculateTotal(Customer customer) {
        BigDecimal total = new BigDecimal("0");
        for (Membership membership : this.memberships)
            total = total.add(membership.getPrice());

        return DiscountStrategy.totalDiscounted(total, this.discounts, customer);
    }
}
