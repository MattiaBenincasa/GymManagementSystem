package DomainModel.PurchaseItems.PurchaseCalculator;

public interface DiscountStrategy {

    float applyDiscount(float price);

    default float percentageDiscount(float price, float percentage) {
        float discountedPrice = price*(1-percentage);
        if (discountedPrice <= 0)
            throw new IllegalArgumentException("Invalid discount: price must be greater than 0 euro.");
        return price*(1-percentage);
    }

    default float fixedDiscount(float price, float discountInEuro) {
        float discountedPrice = price - discountInEuro;
        if (discountedPrice <= 0)
            throw new IllegalArgumentException("Invalid discount: price must be greater than 0 euro.");
        return discountedPrice;
    }

}
