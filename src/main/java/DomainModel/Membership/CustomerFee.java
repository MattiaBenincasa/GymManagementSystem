package DomainModel.Membership;

import DomainModel.RegistrationFee;
import DomainModel.Users.Customer;

import java.time.LocalDate;

public class CustomerFee {
    private int id;
    private final Customer customer;
    private final LocalDate startDate;
    private final LocalDate expiryDate;

    public CustomerFee(int id, LocalDate startDate, LocalDate expiryDate, Customer customer) {
        this.id = id;
        this.startDate = startDate;
        this.expiryDate = expiryDate;
        this.customer = customer;
    }

    public CustomerFee(LocalDate startDate, Customer customer) {
        this.customer = new Customer(customer);
        this.startDate = startDate;
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

    public int getId() {
        return this.id;
    }

}
