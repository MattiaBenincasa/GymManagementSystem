package DomainModel.PurchaseItems;

import DomainModel.Membership.Membership;
import DomainModel.PurchaseItems.PurchaseCalculator.DiscountStrategy;

import java.util.ArrayList;

public class SinglePurchase {
    private Membership membership;
    private final ArrayList<DiscountStrategy> discounts;

    public SinglePurchase(Membership membership) {
        this.membership = membership.copy();
        this.discounts = new ArrayList<>();
    }

    public void addDiscount(DiscountStrategy discountStrategy) {
        this.discounts.add(discountStrategy);
    }

    public void removeDiscount(DiscountStrategy discountStrategy) {
        this.discounts.remove(discountStrategy);
    }

    public float calculateTotal() {
        float total = this.membership.getPrice();
        if (!this.discounts.isEmpty())
            for (DiscountStrategy discountStrategy : this.discounts)
                total = discountStrategy.applyDiscount(total);
        return total;
    }

    public Membership getMembership() {
        return this.membership.copy();
    }

    public void setMembership(Membership membership) {
        this.membership = membership.copy();
    }
}
