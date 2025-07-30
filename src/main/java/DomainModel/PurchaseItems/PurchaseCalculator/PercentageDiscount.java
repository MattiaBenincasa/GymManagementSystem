package DomainModel.PurchaseItems.PurchaseCalculator;

public class PercentageDiscount implements PurchaseCalculator {
    private float percentage;

    public PercentageDiscount(float percentage) {
        this.percentage = percentage;
    }

    @Override
    public float executePurchaseStrategy(float price) {
        return price*(1-this.percentage);
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }
}
