package DomainModel.DiscountStrategy;

import DomainModel.Users.Customer;
import DomainModel.Users.CustomerCategory;

import java.math.BigDecimal;

public class CustomerBasedDiscount extends DiscountStrategy{
    private CustomerCategory customerCategory;
    private int percentage;

    public CustomerBasedDiscount(CustomerCategory customerCategory, int percentage, String description) {
        super(description, true);
        this.percentage = percentage;
        this.customerCategory = customerCategory;
    }

    @Override
    BigDecimal applyDiscount(BigDecimal price, Customer customer) {
        BigDecimal discountedPrice = price;
        if (customer.getCustomerCategory() == this.customerCategory)
            discountedPrice = this.percentageDiscount(price, this.percentage);

        return discountedPrice;
    }

    public CustomerCategory getCustomerCategory() {
        return customerCategory;
    }

    public void setCustomerCategory(CustomerCategory customerCategory) {
        this.customerCategory = customerCategory;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }
}
