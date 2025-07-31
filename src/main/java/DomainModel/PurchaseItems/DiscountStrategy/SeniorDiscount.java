package DomainModel.PurchaseItems.DiscountStrategy;

import DomainModel.Users.Customer;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class SeniorDiscount implements DiscountStrategy{
    private final Customer customer;
    private float percentage;

    public SeniorDiscount(Customer customer) {
        this.customer = new Customer(customer);
    }

    @Override
    public float applyDiscount(float price) {
        long customerAge = ChronoUnit.YEARS.between(LocalDate.now(), this.customer.getBirthDate());
        if (customerAge < 60) {
            throw new IllegalStateException("Discount not applicable: customer must be at least 60 years old");
        }
        return this.percentageDiscount(price, this.percentage);
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }
}
