package DomainModel.Membership;

import DomainModel.DiscountStrategy.DiscountStrategy;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Bundle {
    private String name;
    private String description;
    private final ArrayList<Membership> memberships;
    private DiscountStrategy discount;

    public Bundle() {
        this.memberships = new ArrayList<>();
    }

    public void addMembership(Membership membership) {
        this.memberships.add(membership);
    }

    public void remove(Membership membership) {
        this.memberships.remove(membership);
    }

    public BigDecimal calculateTotal() {
        BigDecimal total = new BigDecimal("0");
        for (Membership membership : this.memberships)
            total = total.add(membership.getPrice());

        if (this.discount != null)
            total = discount.applyDiscount(total);
        return total;
    }
}
