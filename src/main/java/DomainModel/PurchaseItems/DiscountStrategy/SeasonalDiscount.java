package DomainModel.PurchaseItems.DiscountStrategy;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;

public class SeasonalDiscount implements DiscountStrategy{
    private String description;
    private final HashSet<Month> monthsWithDiscount;
    private float percentage;

    public SeasonalDiscount(float percentage) {
        this.percentage = percentage;
        this.monthsWithDiscount = new HashSet<>();
    }

    @Override
    public float applyDiscount(float price) {
        float discountedPrice = price;
        if (monthsWithDiscount.contains(LocalDate.now().getMonth()))
            discountedPrice = this.percentageDiscount(discountedPrice, this.percentage);
        return discountedPrice;
    }

    public void addMonth(Month month) {
        this.monthsWithDiscount.add(month);
    }

    public void removeMonth(Month month) {
        this.monthsWithDiscount.remove(month);
    }

    public HashSet<Month> getMonth() {
        return this.monthsWithDiscount;
    }

    public float getPercentage() {
        return percentage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }
}
