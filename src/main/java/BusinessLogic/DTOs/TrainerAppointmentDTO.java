package BusinessLogic.DTOs;

import DomainModel.Membership.CustomerMembership;
import DomainModel.Membership.WRMembershipType;
import DomainModel.Users.Customer;

public class TrainerAppointmentDTO {
    private Customer customer;
    private CustomerMembership customerMembership;
    private int numOfCustomerBookingsDone;
    private WRMembershipType type;



    public TrainerAppointmentDTO(Customer customer) {
        // TODO init object with a single query
    }

    public Customer getCustomer() {
        return customer;
    }

    public CustomerMembership getCustomerMembership() {
        return customerMembership;
    }

    public int getNumOfCustomerBookingsDone() {
        return numOfCustomerBookingsDone;
    }

    public WRMembershipType getType() {
        return type;
    }
}
