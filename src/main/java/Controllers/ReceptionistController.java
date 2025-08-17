package Controllers;

import BusinessLogic.AuthService.Session;
import DTOs.PurchaseDTO;
import BusinessLogic.Purchase.PaymentMethod;
import BusinessLogic.Purchase.PurchaseService;
import BusinessLogic.Users.CustomerService;
import BusinessLogic.Users.StaffService;
import DomainModel.Users.Customer;
import DomainModel.Users.CustomerCategory;
import DomainModel.Users.Staff;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

public class ReceptionistController {
    private final CustomerService customerService;
    private final PurchaseService purchaseService;
    private final StaffService staffService;
    private Session session;

    public ReceptionistController(StaffService staffService, CustomerService customerService, PurchaseService purchaseService) {
        this.customerService = customerService;
        this.staffService = staffService;
        this.purchaseService = purchaseService;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Staff getPersonalInfo() {
        Session.validateSession(this.session);
        return staffService.getStaffByID(this.session.getUserID());
    }

    public Customer completeCustomerCreation(int customerId, String name, String surname, String phoneNumber, LocalDate birthDate, CustomerCategory customerCategory) {
        Session.validateSession(this.session);
        return customerService.completeCustomerCreation(customerId, name, surname, phoneNumber, birthDate, customerCategory);
    }

    public void addMedicalCertificate(int customerID, LocalDate startDate, int duration, boolean isCompetitive) {
        Session.validateSession(this.session);
        customerService.setupCustomerMedicalCertificate(customerID, startDate, duration, isCompetitive);
    }

    public Customer getCustomerByID(int customerID) {
        Session.validateSession(this.session);
        return customerService.getCustomerByID(customerID);
    }

    public void changeCustomerCategory(int customerID, CustomerCategory customerCategory) {
        Session.validateSession(this.session);
        customerService.changeCustomerCategory(customerID, customerCategory);
    }

    public void changeCustomerInfo(int customerID, String username, String name, String surname, String mail, String phoneNumber) {
        Session.validateSession(this.session);
        customerService.changeCustomerInfo(customerID, username, name, surname, mail, phoneNumber);
    }

    public ArrayList<Customer> getAllCustomers() {
        Session.validateSession(this.session);
        return customerService.getAllCustomers();
    }

    public void changePersonalInfo(String username, String name, String surname, String mail, String phoneNumber) {
        Session.validateSession(this.session);
        staffService.changeStaffInfo(this.session.getUserID(), username, name, surname, mail, phoneNumber);
    }

    public void changePassword(String oldPassword, String newPassword) {
        Session.validateSession(this.session);
        staffService.changePassword(this.session.getUserID(), oldPassword, newPassword);
    }

    public void executePurchase(PurchaseDTO purchaseDTO, PaymentMethod paymentMethod) {
        Session.validateSession(this.session);
        purchaseService.executePurchase(purchaseDTO, paymentMethod);
    }

    public BigDecimal calculateTotal(PurchaseDTO purchaseDTO) {
        Session.validateSession(this.session);
        return purchaseService.calculateTotal(purchaseDTO);
    }

}
