package DomainModel.Membership;

import DomainModel.Users.Customer;

import java.time.LocalDate;

public class CustomerMembership {
    Customer customer;
    Membership membership;
    LocalDate expiryDate;

    public CustomerMembership(LocalDate activationDate, Membership membership, Customer customer) {
        this.customer = customer;
        this.membership = membership;
        this.expiryDate = activationDate.plusDays(membership.getDurationInDays());
    }

    public Customer getCustomer() {
        return customer;
    }

    public Membership getMembership() {
        return membership;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }
}
