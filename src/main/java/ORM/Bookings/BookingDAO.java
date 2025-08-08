package ORM.Bookings;

import BusinessLogic.DTOs.ClassBookingInfo;
import BusinessLogic.Exceptions.DAOException;
import DomainModel.Bookings.Booking;
import DomainModel.DailyEvents.DailyClass;
import DomainModel.Users.Customer;
import ORM.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Date;

public class BookingDAO {
    private final Connection connection;


    public BookingDAO() {
        this.connection = ConnectionManager.getSingleInstance().getConnection();
    }

    public void createBooking(Booking booking) {}

    public void deleteBooking(Booking booking) {}

    public ClassBookingInfo getClassBookingInfo(Customer customer, DailyClass dailyClass) {
        String sql = """
        SELECT 
            cm.expDate AS membershipExpiry,
            c.weeklyAccess AS weeklyBookingLimit,
            COUNT(b1.customer_id) AS totalBookingsInClass,
            (
                SELECT COUNT(*) FROM Booking b2
                JOIN DailyClass dc2 ON b2.dailyclass_id = dc2.id
                WHERE b2.customer_id = ? 
                AND dc2.course_id = dc.course_id
                AND EXTRACT(WEEK FROM dc2.day) = EXTRACT(WEEK FROM dc.day)
            ) AS bookingsDoneByCustomerThisWeek,
            dc.maxParticipants AS classMaxParticipants
        FROM DailyClass dc
        LEFT JOIN CourseMembership c ON c.course = dc.course_id
        LEFT JOIN CustomerMembership cm ON cm.membership_id = c.id AND cm.customer_id = ?
        LEFT JOIN Booking b1 ON b1.dailyclass_id = dc.id
        WHERE dc.id = ?
        GROUP BY cm.expDate, c.weeklyAccess, dc.maxParticipants, dc.course_id, dc.day
        """;

        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {

            stmt.setInt(1, customer.getId());
            stmt.setInt(2, customer.getId());
            stmt.setInt(3, dailyClass.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // membershipExpiry is null if membershipExp is null -> customer has not the specific membership
                    Date membershipExp = rs.getDate("membershipExpiry");
                    LocalDate membershipExpiry = (membershipExp != null) ? membershipExp.toLocalDate() : null;

                    int weeklyBookingLimit = rs.getInt("weeklyBookingLimit");
                    int totalBookingsInClass = rs.getInt("totalBookingsInClass");
                    int bookingsDoneByCustomer = rs.getInt("bookingsDoneByCustomerThisWeek");
                    int maxParticipants = rs.getInt("classMaxParticipants");

                    return new ClassBookingInfo.Builder()
                            .membershipExpiry(membershipExpiry)
                            .weeklyBookingLimit(weeklyBookingLimit)
                            .totalBookingsInClass(totalBookingsInClass)
                            .bookingsDoneByCustomerThisWeek(bookingsDoneByCustomer)
                            .classMaxParticipants(maxParticipants)
                            .build();
                } else {
                    throw new SQLException("No booking info found for class ID " + dailyClass.getId());
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error during SELECT: " + e.getMessage(), e);
        }
    }



}
