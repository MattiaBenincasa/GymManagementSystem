package BusinessLogic.Bookings;

import BusinessLogic.DTOs.ClassBookingInfo;
import BusinessLogic.DTOs.CustomerInfo;
import BusinessLogic.Exceptions.LateCancellationException;
import BusinessLogic.Validators.*;
import DomainModel.Bookings.Booking;
import DomainModel.DailyEvents.DailyClass;
import DomainModel.Users.Customer;
import ORM.Users.CustomerDAO;
import ORM.Users.UserDAO;
import ORM.Bookings.BookingDAO;
import java.time.LocalTime;
import java.util.List;

public class ClassBookingService {
    private final BookingDAO bookingDAO;
    private final UserDAO userDAO;
    private final CustomerDAO customerDAO;

    public ClassBookingService(BookingDAO bookingDAO, UserDAO userDAO, CustomerDAO customerDAO) {
        this.bookingDAO = bookingDAO;
        this.userDAO = userDAO;
        this.customerDAO = customerDAO;
    }

    /*
    * execute a sequence of validators to check if a specific customer can book a
    * specific daily class. If all validators pass then instantiate a Booking and save
    * it with BookingDAO
    * */
    public void bookAClass(int customerID, DailyClass dailyClass) {
        Customer customer = this.customerDAO.getCustomerByID(customerID);
        if (!dailyClass.isActive())
            throw new IllegalStateException("Daily class selected is cancelled");

        CustomerInfo customerInfo = this.userDAO.getCustomerBookingInfo(customer);
        ClassBookingInfo classBookingInfo = this.bookingDAO.getClassBookingInfo(customer, dailyClass);

        Validator classBookingValidators = new FeeValidator(customerInfo)
                .setNextValidator(new MedCertificateValidator(customerInfo))
                .setNextValidator(new MembershipValidator(classBookingInfo.getMembershipExpiry()))
                .setNextValidator(new CheckBookingsValidator(classBookingInfo));

        classBookingValidators.validate();

        Booking booking = new Booking(customer, dailyClass);
        bookingDAO.createBooking(booking);
    }

    // A customer can delete a class no later than one hour before the class starts.
    public void deleteClassBooking(Booking booking) {
        LocalTime startTime = booking.getDailyClass().getStartTime();

        if (LocalTime.now().plusHours(1).isAfter(startTime))
            throw new LateCancellationException("A booking can be deleted no later than one hour before the class starts.");

        this.bookingDAO.deleteBooking(booking);
    }

    public List<Booking> getAllCustomerBookings(int customerID) {
        return this.bookingDAO.getAllCustomerBookings(customerID);
    }
}
