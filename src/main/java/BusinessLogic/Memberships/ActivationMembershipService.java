package BusinessLogic.Memberships;

import DTOs.CustomerInfo;
import DomainModel.Membership.CustomerFee;
import DomainModel.Membership.CustomerMembership;
import DomainModel.Membership.Membership;
import DomainModel.Users.Customer;
import ORM.Membership.CustomerFeeDAO;
import ORM.Membership.CustomerMembershipDAO;
import ORM.Membership.MembershipDAO;
import ORM.Users.CustomerDAO;

import java.time.LocalDate;
import java.util.ArrayList;


public class ActivationMembershipService {
    private final CustomerDAO customerDAO;
    private final MembershipDAO membershipDAO;
    private final CustomerMembershipDAO customerMembershipDAO;
    private final CustomerFeeDAO customerFeeDAO;

    public ActivationMembershipService(CustomerMembershipDAO customerMembershipDAO, CustomerFeeDAO customerFeeDAO, CustomerDAO customerDAO, MembershipDAO membershipDAO) {
        this.customerMembershipDAO = customerMembershipDAO;
        this.customerFeeDAO = customerFeeDAO;
        this.membershipDAO = membershipDAO;
        this.customerDAO = customerDAO;
    }

    public void activateMembership(LocalDate activationDate, int customerID, int membershipID) {
        Customer customer = this.customerDAO.getCustomerByID(customerID);
        Membership membership = this.membershipDAO.getMembershipByID(membershipID);
        CustomerMembership customerMembership = new CustomerMembership(activationDate, membership, customer);
        this.customerMembershipDAO.createCustomerMembership(customerMembership);
    }

    public void activateFee(LocalDate activationDate, int customerID) {
        Customer customer = this.customerDAO.getCustomerByID(customerID);
        CustomerFee customerFee = new CustomerFee(activationDate, customer);
        this.customerFeeDAO.createCustomerFee(customerFee);
    }

    public ArrayList<CustomerMembership> getAllCustomerMembership(int customerID) {
        Customer customer = this.customerDAO.getCustomerByID(customerID);
        return this.customerMembershipDAO.getAllCustomerMembership(customer);
    }

    public LocalDate getActiveCustomerFeeExpiry(int customerID) {
        Customer customer = this.customerDAO.getCustomerByID(customerID);
        CustomerInfo customerInfo = this.customerDAO.getCustomerBookingInfo(customer);
        return customerInfo.getFeeExpiry();
    }
}
