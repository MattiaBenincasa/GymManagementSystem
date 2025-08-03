package BusinessLogic;

import DomainModel.Membership.CustomerFee;
import DomainModel.Membership.CustomerMembership;
import DomainModel.Membership.Membership;
import DomainModel.Users.Customer;

import java.time.LocalDate;

//service for managing activation/renew membership
public class ActivationMembershipService {

    public void activateMembership(LocalDate activationDate, Customer customer, Membership membership) {
        CustomerMembership customerMembership = new CustomerMembership(activationDate, membership, customer);
        //TODO save customerMembership in DB with DAO
    }

    public void activateFee(LocalDate activationDate, Customer customer) {
        CustomerFee customerFee = new CustomerFee(activationDate, customer);
        //TODO save customerFee in DB with DAO
    }
}
