package DomainModel.PurchaseItems;

import DomainModel.Membership.Membership;
import DomainModel.PurchaseItems.PurchaseCalculator.DiscountStrategy;

import java.util.ArrayList;

public class Bundle {
    private String name;
    private String description;
    private final ArrayList<Membership> memberships;
    private DiscountStrategy discounts;

    public Bundle() {
        this.memberships = new ArrayList<>();
    }

    public void addMembership(Membership membership) {
        this.memberships.add(membership);
    }

    public void remove(Membership membership) {
        this.memberships.remove(membership);
    }

    public float calculateTotal() {
        float total = 0;
        for (Membership membership : this.memberships)
            total += membership.getPrice();

        if (this.discounts != null)
            total = discounts.applyDiscount(total);
        return total;
    }
}
