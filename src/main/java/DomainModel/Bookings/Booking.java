package DomainModel.Bookings;

import DomainModel.DailyEvents.DailyClass;
import DomainModel.Users.Customer;


public class Booking {
    private final Customer customer;
    private final DailyClass dailyClass;

    public Booking(Customer customer, DailyClass dailyClass) {
        this.customer = new Customer(customer);
        this.dailyClass = new DailyClass(dailyClass);
    }

    public Customer getCustomer() {
        return new Customer(customer);
    }

    public DailyClass getDailyClass() {
        return new DailyClass(dailyClass);
    }

}

