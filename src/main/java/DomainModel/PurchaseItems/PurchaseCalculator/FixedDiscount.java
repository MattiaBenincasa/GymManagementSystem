package DomainModel.PurchaseItems.PurchaseCalculator;

public class FixedDiscount implements PurchaseCalculator{
    private float discount;

    public FixedDiscount(float discount) {
        this.discount = discount;
    }

    @Override
    public float executePurchaseStrategy(float price) {
        return price - this.discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

}
