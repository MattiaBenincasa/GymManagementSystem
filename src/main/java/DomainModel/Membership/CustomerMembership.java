package DomainModel.Membership;

import DomainModel.Users.Customer;

import java.time.LocalDate;

public class CustomerMembership {
    private final Customer customer;
    private final Membership membership;
    private final LocalDate startDay;
    private final LocalDate expiryDate;

    public CustomerMembership(LocalDate activationDate, Membership membership, Customer customer) {
        this.customer = customer;
        this.membership = membership;
        this.startDay = activationDate;
        this.expiryDate = activationDate.plusDays(membership.getDurationInDays());
    }

    public Customer getCustomer() {
        return new Customer(this.customer);
    }

    public Membership getMembership() {
        return this.membership.copy();
    }

    public LocalDate getStartDay() {
        return this.startDay;
    }

    public LocalDate getExpiryDate() {
        return this.expiryDate;
    }
}
