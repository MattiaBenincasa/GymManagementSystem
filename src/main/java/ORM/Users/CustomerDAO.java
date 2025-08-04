package ORM.Users;

import BusinessLogic.Exceptions.DAOException;
import DomainModel.Users.Customer;
import DomainModel.Users.CustomerCategory;
import ORM.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class CustomerDAO {
    private final Connection connection;
    private final UserDAO userDAO;

    public CustomerDAO(UserDAO userDAO) {
        this.connection = ConnectionManager.getSingleInstance().getConnection();
        this.userDAO = userDAO;
    }

    public int createCustomer(Customer customer) {
        int userId = this.userDAO.createUser(customer, "CUSTOMER");

        String sql = "INSERT INTO Customers (id, customerCategory) VALUES (?, ?)";

        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setString(2, customer.getCustomerCategory().name());

            statement.executeUpdate();
            return userId;
        } catch (SQLException e) {
            throw new DAOException("Error during INSERT into Customers: " + e.getMessage(), e);
        }
    }

    public void updateCustomer(Customer customer) {
        this.userDAO.updateUser(customer);

        String sql = "UPDATE Customers SET customerCategory = ? WHERE id = ?";

        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setString(1, customer.getCustomerCategory().name());
            statement.setInt(2, customer.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Updating customer failed, no rows affected in Customers table.");
            }
        } catch (SQLException e) {
            throw new DAOException("Error during UPDATE of Customers: " + e.getMessage(), e);
        }
    }

    public Customer getCustomerByID(int customerID) {
        String sql = "SELECT u.id, u.username, u.hashPassword, u.name, u.surname, u.mail, u.phoneNumber, u.birthDate, u.role, c.customerCategory " +
                "FROM \"User\" u JOIN Customers c ON u.id = c.id WHERE u.id = ?";

        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setInt(1, customerID);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Customer customer = new Customer(resultSet.getInt("id"));
                    customer.setUsername(resultSet.getString("username"));
                    customer.setName(resultSet.getString("name"));
                    customer.setSurname(resultSet.getString("surname"));
                    customer.setMail(resultSet.getString("mail"));
                    customer.setPhoneNumber(resultSet.getString("phoneNumber"));
                    customer.setBirthDate(resultSet.getDate("birthDate").toLocalDate());
                    customer.setCustomerCategory(CustomerCategory.valueOf(resultSet.getString("customerCategory")));
                    return customer;
                } else {
                    throw new DAOException("Customer with ID " + customerID + " not found.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error during SELECT from User and Customers: " + e.getMessage(), e);
        }
    }

}
