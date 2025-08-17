package ORM.Users;

import DTOs.CustomerInfo;
import Exceptions.DAOException;
import DomainModel.Users.Customer;
import DomainModel.Users.CustomerCategory;
import DomainModel.Users.MedicalCertificate;
import ORM.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;

public class CustomerDAO {
    private final Connection connection;
    private final UserDAO userDAO;

    public CustomerDAO(UserDAO userDAO) {
        this.connection = ConnectionManager.getSingleInstance().getConnection();
        this.userDAO = userDAO;
    }

    public Customer createCustomer(Customer customer) {
        int userId = this.userDAO.createUser(customer, "CUSTOMER");

        String sql = "INSERT INTO Customers (id, customerCategory) VALUES (?, ?)";
        String sql_med_cert = "INSERT INTO CustomerMedCertificate (customer_id, expiryDate, isCompetitive) VALUES (?, ?, ?)";
        try (PreparedStatement statement = this.connection.prepareStatement(sql);
             PreparedStatement statement_med_cert = this.connection.prepareStatement(sql_med_cert)){
            statement.setInt(1, userId);
            if (customer.getCustomerCategory() != null)
                statement.setString(2, customer.getCustomerCategory().name());
            else statement.setNull(2, Types.VARCHAR);

            statement.executeUpdate();

            statement_med_cert.setInt(1, userId);
            if (customer.getMedicalCertificate() != null){
                statement_med_cert.setDate(2, Date.valueOf(customer.getMedicalCertificate().getExpiryDate()));
                statement_med_cert.setBoolean(3, customer.getMedicalCertificate().isCompetitive());
            } else {
                statement_med_cert.setNull(2, Types.DATE);
                statement_med_cert.setNull(3, Types.BOOLEAN);
            }
            statement_med_cert.executeUpdate();
            return new Customer(userId, customer);
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

            if (customer.getMedicalCertificate() != null)
                this.updateCustomerMedCertificate(customer.getId(), customer.getMedicalCertificate());

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
                    Customer customer = new Customer();
                    customer.setUsername(resultSet.getString("username"));
                    customer.setPasswordHash(resultSet.getString("hashpassword"));
                    customer.setName(resultSet.getString("name"));
                    customer.setSurname(resultSet.getString("surname"));
                    customer.setMail(resultSet.getString("mail"));
                    customer.setPhoneNumber(resultSet.getString("phoneNumber"));
                    if (resultSet.getDate("birthDate")!=null)
                        customer.setBirthDate(resultSet.getDate("birthDate").toLocalDate());
                    else customer.setBirthDate(null);
                    if (resultSet.getString("customerCategory")!= null)
                        customer.setCustomerCategory(CustomerCategory.valueOf(resultSet.getString("customerCategory")));
                    else customer.setCustomerCategory(null);
                    customer.setMedicalCertificate(this.getMedicalCertificateByCustomerID(customerID));
                    return new Customer(resultSet.getInt("id"), customer);
                } else {
                    throw new DAOException("Customer with ID " + customerID + " not found.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error during SELECT from User and Customers: " + e.getMessage(), e);
        }
    }

    private void updateCustomerMedCertificate(int customerID, MedicalCertificate medicalCertificate) {
        String sql = "UPDATE customerMedCertificate SET expirydate=?, iscompetitive=? WHERE customer_id = ?";

        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setDate(1, Date.valueOf(medicalCertificate.getExpiryDate()));
            statement.setBoolean(2, medicalCertificate.isCompetitive());
            statement.setInt(3, customerID);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Updating customer failed, no rows affected in Customers table.");
            }
        } catch (SQLException e) {
            throw new DAOException("Error during UPDATE from customerMedCertificate: " + e.getMessage(), e);
        }
    }

    private MedicalCertificate getMedicalCertificateByCustomerID(int customerID) {
        String sql = "SELECT expirydate, iscompetitive FROM customerMedCertificate WHERE customer_id = ?";

        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setInt(1, customerID);

            try (ResultSet resultSet = statement.executeQuery() ) {
                if (resultSet.next()) {
                    Date expiryDate = resultSet.getDate("expirydate");
                    boolean isCompetitive = resultSet.getBoolean("iscompetitive");
                    if (resultSet.wasNull())
                        return null;
                    else
                        return new MedicalCertificate(expiryDate.toLocalDate(), isCompetitive);
                } else {
                    return null;
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Error during UPDATE from customerMedCertificate: " + e.getMessage(), e);
        }
    }

    public ArrayList<Customer> getAllCustomers() {return null;}

    public CustomerInfo getCustomerBookingInfo(Customer customer) {
        return this.userDAO.getCustomerBookingInfo(customer);
    }
}
