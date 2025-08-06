package BusinessLogic;

import BusinessLogic.Exceptions.LateBookingDeletionException;
import DomainModel.Booking;
import DomainModel.DailyClass;
import DomainModel.Users.Customer;
import ORM.bookings.BookingDAO;

import java.time.LocalTime;

public class ClassBookingService {
    private final BookingDAO bookingDAO;

    public ClassBookingService(BookingDAO bookingDAO) {
        this.bookingDAO = bookingDAO;
    }

    /*
    * execute a sequence of validators to check if a specific customer can book a
    * specific daily class. If all validators pass then instantiate a Booking and save
    * it with BookingDAO
    * */
    public void bookAClass(Customer customer, DailyClass dailyClass) {
        //TODO execute validators

        Booking booking = new Booking(customer, dailyClass);
        bookingDAO.createBooking(booking);
    }

    // A customer can cancel a class no later than one hour before the class starts.
    public void deleteClassBooking(Booking booking) {
        LocalTime startTime = booking.getDailyClass().getTime();

        if (LocalTime.now().plusHours(1).isAfter(startTime))
            throw new LateBookingDeletionException("A booking can be deleted no later than one hour before the class starts.");

        this.bookingDAO.deleteBooking(booking);
    }
}
