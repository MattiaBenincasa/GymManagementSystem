package ORM.bookings;

import DomainModel.Booking;
import ORM.ConnectionManager;

import java.sql.Connection;

public class BookingDAO {
    private final Connection connection;


    public BookingDAO() {
        this.connection = ConnectionManager.getSingleInstance().getConnection();
    }

    public void createBooking(Booking booking) {}

    public void deleteBooking(Booking booking) {}


}
