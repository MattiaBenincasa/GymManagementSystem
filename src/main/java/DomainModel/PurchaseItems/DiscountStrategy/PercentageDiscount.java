package DomainModel.PurchaseItems.DiscountStrategy;

public class PercentageDiscount implements DiscountStrategy {
    private String description;
    private float percentage;

    public PercentageDiscount(float percentage) {
        this.percentage = percentage;
    }

    @Override
    public float applyDiscount(float price) {
        return this.percentageDiscount(price, this.percentage);
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPercentage() {
        return percentage;
    }
}
