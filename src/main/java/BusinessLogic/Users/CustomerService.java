package BusinessLogic.Users;

import BusinessLogic.AuthService.PasswordUtils;
import DomainModel.Users.Customer;
import DomainModel.Users.CustomerCategory;
import DomainModel.Users.MedicalCertificate;
import ORM.Users.CustomerDAO;
import ORM.Users.UserDAO;

import java.time.LocalDate;
import java.util.ArrayList;

public class CustomerService {
    private final CustomerDAO customerDAO;
    private final UserDAO userDAO;

    public CustomerService(CustomerDAO customerDAO, UserDAO userDAO) {
        this.customerDAO = customerDAO;
        this.userDAO = userDAO;
    }

    // initialize customer profile with basic info
    public Customer createCustomer(String username, String password, String mail) {
        Customer customer = new Customer();
        customer.setUsername(username);
        customer.setPassword(password);
        customer.setMail(mail);

        return customerDAO.createCustomer(customer);
    }

    // complete customer profile with all customer info
    public Customer completeCustomerCreation(int customerId, String name, String surname, String phoneNumber, LocalDate birthDate, CustomerCategory customerCategory) {
        Customer customer = this.customerDAO.getCustomerByID(customerId);
        customer.setName(name);
        customer.setSurname(surname);
        customer.setPhoneNumber(phoneNumber);
        customer.setBirthDate(birthDate);
        customer.setCustomerCategory(customerCategory);
        this.customerDAO.updateCustomer(customer);
        return customer;
    }

    public void setupCustomerMedicalCertificate(int customerID, LocalDate startDate, int duration, boolean isCompetitive) {
        Customer customer = this.customerDAO.getCustomerByID(customerID);
        MedicalCertificate medicalCertificate = new MedicalCertificate(startDate, duration, isCompetitive);
        customer.setMedicalCertificate(medicalCertificate);
        this.customerDAO.updateCustomer(customer);
    }

    public Customer getCustomerByID(int customerID) {
        return this.customerDAO.getCustomerByID(customerID);
    }

    public void changeCustomerCategory(int customerID, CustomerCategory customerCategory) {
        Customer customer = this.customerDAO.getCustomerByID(customerID);
        customer.setCustomerCategory(customerCategory);
        this.customerDAO.updateCustomer(customer);
    }

    public void changeCustomerInfo(int customerID, String username, String name, String surname, String mail, String phoneNumber) {
        UserService.updateUserInfo(this.userDAO,
                this.customerDAO.getCustomerByID(customerID),
                username,
                name,
                surname,
                mail,
                phoneNumber);
    }

    public void changePassword(int customerID, String oldPassword, String newPassword) {
        Customer customer = this.customerDAO.getCustomerByID(customerID);
        PasswordUtils.checkPassword(oldPassword, customer.getHashPassword());
        UserService.changePassword(this.userDAO, customer, newPassword);
    }

    public ArrayList<Customer> getAllCustomers() {
        return this.customerDAO.getAllCustomers();
    }

}
