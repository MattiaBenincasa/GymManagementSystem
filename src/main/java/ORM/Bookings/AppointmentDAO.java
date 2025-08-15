package ORM.Bookings;
import BusinessLogic.DTOs.WeightRoomBookingInfo;
import Exceptions.DAOException;
import DomainModel.Bookings.Appointment;
import DomainModel.DailyEvents.TrainerAvailability;
import DomainModel.Membership.WRMembershipType;
import DomainModel.Users.Customer;
import ORM.ConnectionManager;
import ORM.DailyEvents.TrainerAvailabilityDAO;
import ORM.Users.CustomerDAO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;


public class AppointmentDAO {
    private Connection connection;
    private final TrainerAvailabilityDAO trainerAvailabilityDAO;
    private final CustomerDAO customerDAO;

    public AppointmentDAO(TrainerAvailabilityDAO trainerAvailabilityDAO, CustomerDAO customerDAO) {
        this.connection = ConnectionManager.getSingleInstance().getConnection();
        this.trainerAvailabilityDAO = trainerAvailabilityDAO;
        this.customerDAO = customerDAO;
    }

    public void createAppointment(Appointment appointment) {
        String sql = "INSERT INTO Appointment (traineravailability_id, customer_id, notes) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, appointment.getTrainerAvailability().getId());
            statement.setInt(2, appointment.getCustomer().getId());
            statement.setString(3, appointment.getAppointmentPurpose());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error during INSERT into Appointment: " + e.getMessage(), e);
        }
    }

    public void deleteAppointment(Appointment appointment) {
        String sql = "DELETE FROM Appointment WHERE traineravailability_id = ? AND customer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, appointment.getTrainerAvailability().getId());
            statement.setInt(2, appointment.getCustomer().getId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Deletion of appointment failed, no rows affected. Appointment not found.");
            }
        } catch (SQLException e) {
            throw new DAOException("Error during DELETE from Appointment: " + e.getMessage(), e);
        }
    }

    public void updateAppointment(Appointment appointment) {
        String sql = "UPDATE Appointment SET notes = ? WHERE traineravailability_id = ? AND customer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, appointment.getAppointmentPurpose());
            statement.setInt(2, appointment.getTrainerAvailability().getId());
            statement.setInt(3, appointment.getCustomer().getId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Updating appointment failed, no rows affected. Appointment not found.");
            }
        } catch (SQLException e) {
            throw new DAOException("Error during UPDATE on Appointment: " + e.getMessage(), e);
        }
    }

    public Appointment getAppointment(int trainerAvailabilityID, int customerID) {
        String sql = "SELECT traineravailability_id, customer_id, notes FROM Appointment WHERE traineravailability_id = ? AND customer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, trainerAvailabilityID);
            statement.setInt(2, customerID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String notes = resultSet.getString("notes");
                    TrainerAvailability ta = this.trainerAvailabilityDAO.getTrainerAvailabilityByID(trainerAvailabilityID);
                    Customer customer = this.customerDAO.getCustomerByID(customerID);
                    Appointment appointment = new Appointment(customer, ta);
                    appointment.setAppointmentPurpose(notes);
                    return appointment;
                } else {
                    throw new DAOException("Appointment not found.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error during SELECT from Appointment: " + e.getMessage(), e);
        }
    }

    public WeightRoomBookingInfo getWeightRoomBookingInfo(Customer customer, LocalDate appointmentDate) {
        String sql = """
            SELECT
                cm.expDate AS membershipExpiry,
                wrm.type AS membershipType,
                (
                    SELECT COUNT(*)
                    FROM Appointment a
                    JOIN TrainerAvailability ta ON a.traineravailability_id = ta.id
                    WHERE a.customer_id = ?
                    AND EXTRACT(MONTH FROM ta.day) = ?
                    AND EXTRACT(YEAR FROM ta.day) = ?
                ) AS monthlyAppointments
            FROM CustomerMembership cm
            JOIN WRMembership wrm ON cm.membership_id = wrm.id
            WHERE cm.customer_id = ?
            AND cm.expDate > ?
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customer.getId());
            statement.setInt(2, appointmentDate.getMonthValue());
            statement.setInt(3, appointmentDate.getYear());
            statement.setInt(4, customer.getId());
            statement.setDate(5, java.sql.Date.valueOf(appointmentDate));

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    LocalDate expiry = resultSet.getDate("membershipExpiry").toLocalDate();
                    WRMembershipType type = WRMembershipType.valueOf(resultSet.getString("membershipType"));
                    int monthlyAppointments = resultSet.getInt("monthlyAppointments");

                    return new WeightRoomBookingInfo(expiry, type, monthlyAppointments);
                } else {
                    throw new DAOException("No active WRMembership found for the customer.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error retrieving WeightRoomBookingInfo: " + e.getMessage(), e);
        }
    }

    public ArrayList<Appointment> getAllDailyAppointment(int trainerID) {
        ArrayList<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.traineravailability_id, a.customer_id, a.notes FROM Appointment a " +
                "INNER JOIN TrainerAvailability ta ON a.traineravailability_id = ta.id " +
                "WHERE ta.trainer_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, trainerID);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int trainerAvailabilityId = resultSet.getInt("traineravailability_id");
                    int customerId = resultSet.getInt("customer_id");
                    String notes = resultSet.getString("notes");

                    TrainerAvailability trainerAvailability = trainerAvailabilityDAO.getTrainerAvailabilityByID(trainerAvailabilityId);
                    Customer customer = customerDAO.getCustomerByID(customerId);
                    Appointment appointment = new Appointment(customer, trainerAvailability);
                    appointment.setAppointmentPurpose(notes);
                    appointments.add(appointment);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error during SELECT from Appointment by Trainer ID: " + e.getMessage(), e);
        }
        return appointments;
    }


    public ArrayList<Appointment> getAllCustomerDailyAppointment(int customerID) {
        ArrayList<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.traineravailability_id, a.customer_id, a.notes FROM Appointment a " +
                "INNER JOIN TrainerAvailability ta ON a.traineravailability_id = ta.id " +
                "WHERE a.customer_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerID);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int trainerAvailabilityId = resultSet.getInt("traineravailability_id");
                    int customerId = resultSet.getInt("customer_id");
                    String notes = resultSet.getString("notes");

                    TrainerAvailability trainerAvailability = trainerAvailabilityDAO.getTrainerAvailabilityByID(trainerAvailabilityId);
                    Customer customer = customerDAO.getCustomerByID(customerId);
                    Appointment appointment = new Appointment(customer, trainerAvailability);
                    appointment.setAppointmentPurpose(notes);
                    appointments.add(appointment);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error during SELECT from Appointment by Trainer ID: " + e.getMessage(), e);
        }
        return appointments;
    }
}
