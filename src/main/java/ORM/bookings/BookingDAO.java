package ORM.bookings;

import BusinessLogic.DTOs.ClassBookingInfo;
import BusinessLogic.DTOs.CustomerInfo;
import DomainModel.Booking;
import DomainModel.DailyClass;
import DomainModel.Users.Customer;
import ORM.ConnectionManager;

import java.sql.Connection;

public class BookingDAO {
    private final Connection connection;


    public BookingDAO() {
        this.connection = ConnectionManager.getSingleInstance().getConnection();
    }

    public void createBooking(Booking booking) {}

    public void deleteBooking(Booking booking) {}

    public ClassBookingInfo getClassBookingInfo(Customer customer, DailyClass dailyClass) {}

    public CustomerInfo getCustomerBookingInfo(Customer customer) {}
}
