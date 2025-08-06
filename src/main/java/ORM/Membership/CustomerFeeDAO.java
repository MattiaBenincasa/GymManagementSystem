package ORM.Membership;

import BusinessLogic.Exceptions.DAOException;
import DomainModel.Membership.CustomerFee;
import ORM.ConnectionManager;
import ORM.Users.CustomerDAO;
import ORM.Users.UserDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomerFeeDAO {
    private Connection connection;
    private CustomerDAO customerDAO;

    public CustomerFeeDAO(UserDAO userDAO) {
        this.connection = ConnectionManager.getSingleInstance().getConnection();
        this.customerDAO = new CustomerDAO(userDAO);
    }

    public int createCustomerFee(CustomerFee customerFee) {
        String sql = "INSERT INTO CustomerFee (customer_id, start_date, expiry_date) VALUES (?, ?, ?)";

        try (PreparedStatement statement = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, customerFee.getCustomer().getId());
            statement.setDate(2, java.sql.Date.valueOf(customerFee.getStartDate()));
            statement.setDate(3, java.sql.Date.valueOf(customerFee.getExpiryDate()));

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new DAOException("Creating customer fee failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error during INSERT into CustomerFee: " + e.getMessage(), e);
        }
    }

    public CustomerFee getCustomerFeeByID(int feeId) {
        String sql = "SELECT id, customer_id, start_date, expiry_date FROM CustomerFee WHERE id = ?";

        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setInt(1, feeId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new CustomerFee(resultSet.getInt("id"),
                            resultSet.getDate("start_date").toLocalDate(),
                            resultSet.getDate("expiry_date").toLocalDate(),
                            customerDAO.getCustomerByID(resultSet.getInt("customer_id")));
                } else {
                    throw new DAOException("CustomerFee with ID " + feeId + " not found.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error during SELECT from CustomerFee: " + e.getMessage(), e);
        }
    }
}