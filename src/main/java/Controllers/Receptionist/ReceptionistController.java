package Controllers.Receptionist;

import BusinessLogic.DTOs.PurchaseDTO;
import BusinessLogic.Purchase.PaymentMethod;
import BusinessLogic.Purchase.PurchaseService;
import BusinessLogic.Users.CustomerService;
import BusinessLogic.Users.StaffService;
import DomainModel.Users.Customer;
import DomainModel.Users.CustomerCategory;

import java.time.LocalDate;
import java.util.ArrayList;

public class ReceptionistController {
    private final CustomerService customerService;
    private final PurchaseService purchaseService;
    private final StaffService staffService;

    public ReceptionistController(StaffService staffService, CustomerService customerService, PurchaseService purchaseService) {
        this.customerService = customerService;
        this.staffService = staffService;
        this.purchaseService = purchaseService;
    }

    public Customer completeCustomerCreation(int customerId, String name, String surname, String phoneNumber, LocalDate birthDate, CustomerCategory customerCategory) {
        return customerService.completeCustomerCreation(customerId, name, surname, phoneNumber, birthDate, customerCategory);
    }

    public void addMedicalCertificate(int customerID, LocalDate startDate, int duration, boolean isCompetitive) {
        customerService.setupCustomerMedicalCertificate(customerID, startDate, duration, isCompetitive);
    }

    public Customer getCustomerByID(int customerID) {
        return customerService.getCustomerByID(customerID);
    }

    public void changeCustomerCategory(int customerID, CustomerCategory customerCategory) {
        customerService.changeCustomerCategory(customerID, customerCategory);
    }

    public void changeCustomerInfo(int customerID, String username, String name, String surname, String mail, String phoneNumber) {
        customerService.changeCustomerInfo(customerID, username, name, surname, mail, phoneNumber);
    }

    public ArrayList<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    public void changeStaffInfo(int staffID, String username, String name, String surname, String mail, String phoneNumber) {
        staffService.changeStaffInfo(staffID, username, name, surname, mail, phoneNumber);
    }

    public void changePassword(int staffID, String oldPassword, String newPassword) {
        staffService.changePassword(staffID, oldPassword, newPassword);
    }

    // Metodi di PurchaseService
    public void executePurchase(PurchaseDTO purchaseDTO, PaymentMethod paymentMethod) {
        purchaseService.executePurchase(purchaseDTO, paymentMethod);
    }
}
