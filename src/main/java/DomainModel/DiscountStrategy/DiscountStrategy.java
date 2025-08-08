package DomainModel.DiscountStrategy;

import DomainModel.Users.Customer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class DiscountStrategy {
    private int id;
    private String description;
    private boolean isSpecialOffer;

    public DiscountStrategy(int id, String description, boolean isSpecialOffer) {
        this.id = id;
        this.description = description;
        this.isSpecialOffer = isSpecialOffer;
    }

    public DiscountStrategy(String description, boolean isSpecialOffer) {
        this.description = description;
        this.isSpecialOffer = isSpecialOffer;
    }

    abstract BigDecimal applyDiscount(BigDecimal price, Customer customer);
    public abstract int getValue();

    protected BigDecimal percentageDiscount(BigDecimal price, int percentage) {
        BigDecimal discountedPrice = price.multiply(new BigDecimal(1-percentage));
        if (discountedPrice.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Invalid discount: price must be greater than 0 euro.");
        return discountedPrice;
    }

    protected BigDecimal fixedDiscount(BigDecimal price, BigDecimal discountInEuro) {
        BigDecimal discountedPrice = price.subtract(discountInEuro);
        if (discountedPrice.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Invalid discount: discounted price must be greater than 0 euro.");
        return discountedPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIsSpecialOffer(boolean isSpecialOffer) {
        this.isSpecialOffer = isSpecialOffer;
    }

    public boolean isSpecialOffer() {
        return this.isSpecialOffer;
    }

     public static BigDecimal totalDiscounted(BigDecimal price, ArrayList<DiscountStrategy> discounts, Customer customer) {
        BigDecimal discountedPrice = price;
        Map<Boolean, List<DiscountStrategy>> partitionedDiscounts = discounts.stream()
                .collect(Collectors.partitioningBy(DiscountStrategy::isSpecialOffer));

        List<DiscountStrategy> specialOffers = partitionedDiscounts.get(true);
        List<DiscountStrategy> nonSpecialOffers = partitionedDiscounts.get(false);

        //apply all nonSpecial offer discounts -> they are cumulative

        for (DiscountStrategy discount : nonSpecialOffers)
            discountedPrice = discount.applyDiscount(discountedPrice, customer);

        Map<DiscountStrategy, BigDecimal> specialDiscounts = new HashMap<>();

        //get the best specialOffer discount
        for (DiscountStrategy discount : specialOffers)
            specialDiscounts.put(discount, discount.applyDiscount(discountedPrice, customer));

        return specialDiscounts.values().stream().min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
    }

    public int getId() {
        return id;
    }
}
