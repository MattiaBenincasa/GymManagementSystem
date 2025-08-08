package ORM.Bookings;
import BusinessLogic.DTOs.WeightRoomBookingInfo;
import DomainModel.Bookings.Appointment;
import DomainModel.Membership.WRMembershipType;
import DomainModel.Users.Customer;
import ORM.ConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;


public class AppointmentDAO {
    private Connection connection;

    public AppointmentDAO() {
        this.connection = ConnectionManager.getSingleInstance().getConnection();
    }

    public void createAppointment(Appointment appointment) {}

    public void deleteAppointment(Appointment appointment) {}

    public void updateAppointment(Appointment appointment) {}

    public WeightRoomBookingInfo getWeightRoomBookingInfo(Customer customer, LocalDate appointmentDate) {
        String sql = """
        SELECT 
            cm.expDate AS membershipExpiry,
            wrm.type AS membershipType,
            (
                SELECT COUNT(*) 
                FROM Appointment a
                WHERE a.customer_id = ? 
                AND EXTRACT(MONTH FROM a.day) = EXTRACT(MONTH FROM ?) 
                AND EXTRACT(YEAR FROM a.day) = EXTRACT(YEAR FROM ?)
            ) AS monthlyAppointments
        FROM CustomerMembership cm
        JOIN WRMembership wrm ON cm.membership_id = wrm.id
        WHERE cm.customer_id = ?
        AND cm.expDate > ?
        """;

        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            stmt.setInt(1, customer.getId());
            stmt.setDate(2, Date.valueOf(appointmentDate));
            stmt.setDate(3, Date.valueOf(appointmentDate));
            stmt.setInt(4, customer.getId());
            stmt.setDate(5, Date.valueOf(appointmentDate));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    LocalDate membershipExpiry = rs.getDate("membershipExpiry").toLocalDate();
                    WRMembershipType type = WRMembershipType.valueOf(rs.getString("membershipType"));
                    int monthlyAppointments = rs.getInt("monthlyAppointments");

                    return new WeightRoomBookingInfo(membershipExpiry, type, monthlyAppointments);
                } else {
                    throw new IllegalStateException("No valid WRMembership found for customer.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Appointment> getAllDailyAppointment(int trainerID) {}


}
