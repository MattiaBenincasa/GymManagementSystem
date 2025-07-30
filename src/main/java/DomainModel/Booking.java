package DomainModel;

import DomainModel.Users.Customer;

import java.time.LocalDateTime;

public class Booking {
    private final Customer customer;
    private final DailyClass dailyClass;
    private final LocalDateTime timestamp;
    private boolean isConfirmed;

    public Booking(Customer customer, DailyClass dailyClass) {
        this.customer = new Customer(customer);
        this.dailyClass = new DailyClass(dailyClass);
        this.timestamp = LocalDateTime.now();
    }

    public Customer getCustomer() {
        return new Customer(customer);
    }

    public DailyClass getDailyClass() {
        return new DailyClass(dailyClass);
    }

    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    // once set isConfirmed=true, you cannot change it anymore
    public void confirmBooking() {
        this.isConfirmed = true;
    }

    public boolean isConfirmed() {
        return this.isConfirmed;
    }
}

