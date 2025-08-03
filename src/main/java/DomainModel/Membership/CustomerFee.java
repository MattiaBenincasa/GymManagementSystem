package DomainModel.Membership;

import DomainModel.RegistrationFee;
import DomainModel.Users.Customer;

import java.time.LocalDate;

public class CustomerFee {
    private final Customer customer;
    private final LocalDate startDate;
    private final LocalDate expiryDate;
    private final RegistrationFee registrationFee;

    public CustomerFee(LocalDate startDate, Customer customer) {
        this.customer = new Customer(customer);
        this.startDate = startDate;
        this.registrationFee = RegistrationFee.getRegistrationFee();
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

    public RegistrationFee getFee() {
        return this.registrationFee;
    }
}
