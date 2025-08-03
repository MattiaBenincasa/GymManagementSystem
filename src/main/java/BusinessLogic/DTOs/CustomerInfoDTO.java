package BusinessLogic.DTOs;

import DomainModel.Membership.CustomerFee;
import DomainModel.Users.Customer;

public class CustomerInfoDTO {
    private final Customer customer;
    private CustomerFee customerFee;

    // TODO this object should be created with one query


    public CustomerInfoDTO(Customer customer) {
        this.customer = customer;
        // TODO create customerFee from DB with DAO
        // TODO if multiple customerFee are retrieved, get the one with
        // TODO startDay after today and endDay before today
    }

    public Customer getCustomer() {
        return customer;
    }

    public CustomerFee getCustomerFee() {
        return customerFee;
    }
}
