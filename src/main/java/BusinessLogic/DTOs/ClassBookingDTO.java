package BusinessLogic.DTOs;

import DomainModel.DailyClass;
import DomainModel.Membership.CustomerMembership;
import DomainModel.Users.Customer;

public class ClassBookingDTO {
    private Customer customer;
    private CustomerMembership customerMembership;
    private int nPlacesAvailable;
    private int numOfCustomerBookingsDone;
    private int numOfCustomerBookingsAllowed;

    public ClassBookingDTO(Customer customer, DailyClass dailyClass) {
        // TODO init object with a single query
    }

    public Customer getCustomer() {
        return customer;
    }

    public CustomerMembership getCustomerMembership() {
        return customerMembership;
    }

    public int getnPlacesAvailable() {
        return nPlacesAvailable;
    }

    public int getNumOfCustomerBookingsDone() {
        return numOfCustomerBookingsDone;
    }

    public int getNumOfCustomerBookingsAllowed() {
        return numOfCustomerBookingsAllowed;
    }
}
