package DomainModel.Membership;

import DomainModel.Fee;
import DomainModel.Users.Customer;

import java.time.LocalDate;

public class CustomerFee {
    private final Customer customer;
    private final LocalDate startDate;
    private final LocalDate expiryDate;
    private final Fee fee;

    public CustomerFee(LocalDate startDate, Customer customer, Fee fee) {
        this.customer = new Customer(customer);
        this.startDate = startDate;
        this.fee = fee;
        this.expiryDate = startDate.plusYears(1);
    }

    public Customer getCustomer() {
        return customer;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public Fee getFee() {
        return fee;
    }
}
