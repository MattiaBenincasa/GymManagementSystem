package DomainModel;

import DomainModel.Users.Customer;

public class Booking {
    Customer customer;
    DailyClass dailyClass;

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

