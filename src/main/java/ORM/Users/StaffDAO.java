package ORM.Users;

import BusinessLogic.Exceptions.DAOException;
import DomainModel.Users.Staff;
import DomainModel.Users.StaffRole;
import ORM.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StaffDAO {
    private final Connection connection;
    private final UserDAO userDAO;

    public StaffDAO(UserDAO userDAO) {
        this.connection = ConnectionManager.getSingleInstance().getConnection();
        this.userDAO = userDAO;
    }

    public Staff createStaff(Staff staff) {
        int id = this.userDAO.createUser(staff, staff.getStaffRole().name());
        return new Staff(id, staff);
    }

    public void updateStaff(Staff staff) {
        this.userDAO.updateUser(staff);
    }

    public Staff getStaffByID(int staffID) {
        String sql = "SELECT u.id, u.username, u.hashPassword, u.name, u.surname, u.mail, u.phoneNumber, u.birthDate, u.role " +
                "FROM \"User\" u WHERE u.id = ?";

        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setInt(1, staffID);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    StaffRole role = StaffRole.valueOf(resultSet.getString("role"));
                    Staff staff = new Staff(role);
                    staff.setUsername(resultSet.getString("username"));
                    staff.setPasswordHash(resultSet.getString("hashpassword"));
                    staff.setName(resultSet.getString("name"));
                    staff.setSurname(resultSet.getString("surname"));
                    staff.setMail(resultSet.getString("mail"));
                    staff.setPhoneNumber(resultSet.getString("phoneNumber"));
                    staff.setBirthDate(resultSet.getDate("birthDate").toLocalDate());
                    return new Staff(id, staff);
                } else {
                    throw new DAOException("Trainer with ID " + staffID + " not found.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error during SELECT from User and Trainer: " + e.getMessage(), e);
        }
    }

    public ArrayList<Staff> getAllReceptionists() {
        String query = "SELECT * FROM \"User\" WHERE role = ?";
        ArrayList<Staff> receptionists = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, StaffRole.RECEPTIONIST.toString());
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    receptionists.add(extractStaffFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error getting all receptionists: " + e.getMessage(), e);
        }
        return receptionists;
    }

    public ArrayList<Staff> getAllAdmins() {
        String query = "SELECT * FROM \"User\" WHERE role = ?";
        ArrayList<Staff> receptionists = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, StaffRole.ADMIN.toString());
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    receptionists.add(extractStaffFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error getting all receptionists: " + e.getMessage(), e);
        }
        return receptionists;
    }

    private Staff extractStaffFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        Staff staff = new Staff(StaffRole.valueOf(rs.getString("role")));
        staff.setUsername(rs.getString("username"));
        staff.setPasswordHash(rs.getString("hashpassword"));
        staff.setMail(rs.getString("mail"));
        staff.setName(rs.getString("name"));
        staff.setSurname(rs.getString("surname"));
        staff.setPhoneNumber(rs.getString("phoneNumber"));
        staff.setBirthDate(rs.getDate("birthDate").toLocalDate());

        return new Staff(id, staff);
    }

}
