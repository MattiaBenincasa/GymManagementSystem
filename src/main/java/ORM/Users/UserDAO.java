package ORM.Users;

import BusinessLogic.DTOs.CustomerInfo;
import BusinessLogic.Exceptions.DAOException;
import DomainModel.Users.Customer;
import DomainModel.Users.User;
import ORM.ConnectionManager;

import java.sql.*;
import java.time.LocalDate;

public class UserDAO {
    private final Connection connection;

    public UserDAO() {
        this.connection = ConnectionManager.getSingleInstance().getConnection();
    }

    public String getHashPasswordFromUsername(String username) {
        String sql = "SELECT hashPassword FROM \"User\" WHERE username = ?";

        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("hashPassword");
                } else {
                    throw new DAOException("User with username " + username + " not found.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error during SELECT: " + e.getMessage(), e);
        }
    }

    public int getIdFromUsername(String username) {
        String sql = "SELECT id FROM \"User\" WHERE username = ?";
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                } else {
                    throw new DAOException("User with username " + username + " not found.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error during SELECT: " + e.getMessage(), e);
        }
    }

    public String getRoleFromUsername(String username) {
        String sql = "SELECT role FROM \"User\" WHERE username = ?";
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("role");
                } else {
                    throw new DAOException("User with username " + username + " not found.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error during SELECT: " + e.getMessage(), e);
        }
    }

    public int createUser(User user, String role) {
        String sql = "INSERT INTO \"User\" (username, hashpassword, name, surname, mail, phonenumber, birthdate, role)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?) ";

        try (PreparedStatement statement = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getHashPassword());
            statement.setString(3, user.getName());
            statement.setString(4, user.getSurname());
            statement.setString(5, user.getMail());
            statement.setString(6, user.getPhoneNumber());
            if (user.getBirthDate()!= null)
                statement.setDate(7, Date.valueOf(user.getBirthDate()));
            else statement.setNull(7, Types.DATE);
            statement.setString(8, role);
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();

            if (generatedKeys.next()){
                return generatedKeys.getInt(1);
            }else{
                throw new DAOException("No key generated");
            }

        } catch (SQLException e) {
            throw new DAOException("Error during INSERT: " + e.getMessage(), e);
        }
    }

    public void updateUser(User user) {
        String sql = "UPDATE \"User\" SET username = ?, hashPassword = ?, name = ?, surname = ?, mail = ?, phoneNumber = ?, birthDate = ? WHERE id = ?";

        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getHashPassword());
            statement.setString(3, user.getName());
            statement.setString(4, user.getSurname());
            statement.setString(5, user.getMail());
            statement.setString(6, user.getPhoneNumber());
            statement.setDate(7, Date.valueOf(user.getBirthDate()));
            statement.setInt(8, user.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Updating user failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DAOException("Error during UPDATE: " + e.getMessage(), e);
        }
    }

    public void deleteUser(int userId) {
        String sql = "DELETE FROM \"User\" WHERE id = ?";

        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setInt(1, userId);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Deleting user failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DAOException("Error during DELETE: " + e.getMessage(), e);
        }
    }

    public CustomerInfo getCustomerBookingInfo(Customer customer) {
        String query = """
            SELECT cmc.expiryDate AS medCertExpiry, cf.expiry_date AS feeExpiry, cf.start_date AS feeBegin
            FROM Customers c
            LEFT JOIN CustomerMedCertificate cmc ON c.id = cmc.customer_id
            LEFT JOIN CustomerFee cf ON c.id = cf.customer_id
            WHERE cmc.customer_id = ?
            AND cf.start_date <= ?
            AND cf.expiry_date > ?
            LIMIT 1;
        """;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, customer.getId());
            statement.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
            statement.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    LocalDate medCert;
                    LocalDate feeBegin;
                    LocalDate feeExpiry;
                    if (rs.getDate("medCertExpiry")==null)
                        medCert = null;
                    else medCert = rs.getDate("medCertExpiry").toLocalDate();
                    if (rs.getDate("feeBegin").toLocalDate()==null)
                        feeBegin = null;
                    else feeBegin = rs.getDate("feeBegin").toLocalDate();
                    if (rs.getDate("feeExpiry").toLocalDate()==null)
                        feeExpiry = null;
                    else feeExpiry = rs.getDate("feeExpiry").toLocalDate();
                    return new CustomerInfo(medCert, feeBegin, feeExpiry);
                } else {
                    throw new IllegalStateException("Customer info not found");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error during SELECT: " + e.getMessage(), e);
        }
    }
}
