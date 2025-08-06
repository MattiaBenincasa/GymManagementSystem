package BusinessLogic;

import DomainModel.Membership.CustomerFee;
import DomainModel.Membership.CustomerMembership;
import DomainModel.Membership.Membership;
import DomainModel.Users.Customer;
import ORM.Membership.CustomerFeeDAO;
import ORM.Membership.CustomerMembershipDAO;

import java.time.LocalDate;


public class ActivationMembershipService {
    private final CustomerMembershipDAO customerMembershipDAO;
    private final CustomerFeeDAO customerFeeDAO;

    public ActivationMembershipService(CustomerMembershipDAO customerMembershipDAO, CustomerFeeDAO customerFeeDAO) {
        this.customerMembershipDAO = customerMembershipDAO;
        this.customerFeeDAO = customerFeeDAO;
    }

    public void activateMembership(LocalDate activationDate, Customer customer, Membership membership) {
        CustomerMembership customerMembership = new CustomerMembership(activationDate, membership, customer);
        this.customerMembershipDAO.createCustomerMembership(customerMembership);
    }

    public void activateFee(LocalDate activationDate, Customer customer) {
        CustomerFee customerFee = new CustomerFee(activationDate, customer);
        this.customerFeeDAO.createCustomerFee(customerFee);
    }
}
