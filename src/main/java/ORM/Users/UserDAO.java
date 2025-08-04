package ORM.Users;

import BusinessLogic.Exceptions.DAOException;
import DomainModel.Users.User;
import ORM.ConnectionManager;

import java.sql.*;

public class UserDAO {
    private final Connection connection;

    public UserDAO() {
        this.connection = ConnectionManager.getSingleInstance().getConnection();
    }

    public String getHashPasswordFromUsername(String username) throws DAOException {
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
            statement.setDate(7, Date.valueOf(user.getBirthDate()));
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
            statement.setInt(9, user.getId());

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

}
