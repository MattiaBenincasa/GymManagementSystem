package DomainModel.PurchaseItems.DiscountStrategy;

public class FixedDiscount implements DiscountStrategy {
    private String description;
    private float discountInEuro;

    public FixedDiscount(float discountInEuro) {
        this.discountInEuro = discountInEuro;
    }

    @Override
    public float applyDiscount(float price) {
        return this.fixedDiscount(price, this.discountInEuro);
    }

    public void setDiscount(float discountInEuro) {
        this.discountInEuro = discountInEuro;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getDiscountInEuro() {
        return discountInEuro;
    }

    public void setDiscountInEuro(float discountInEuro) {
        this.discountInEuro = discountInEuro;
    }
}
