package DomainModel.PurchaseItems;

import DomainModel.Membership.Membership;
import DomainModel.PurchaseItems.PurchaseCalculator.PurchaseCalculator;

public class SingleMembership implements PurchaseBundle{
    private Membership membership;
    private PurchaseCalculator purchaseCalculator;

    public SingleMembership(Membership membership) {
        this.membership = membership.copy();
    }

    public void setPurchaseCalculator(PurchaseCalculator purchaseCalculator) {
        this.purchaseCalculator = purchaseCalculator;
    }

    @Override
    public float calculateTotal() {
        float total = this.membership.getPrice();
        if (this.purchaseCalculator != null)
            total = purchaseCalculator.executePurchaseStrategy(total);
        return total;
    }

    public Membership getMembership() {
        return membership.copy();
    }

    public void setMembership(Membership membership) {
        this.membership = membership.copy();
    }
}
