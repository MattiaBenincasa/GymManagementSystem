package BusinessLogic.Validators;

import DTOs.CustomerInfo;
import Exceptions.ValidatorException;

import java.time.LocalDate;

public class FeeValidator extends Validator{

    private CustomerInfo customerInfo;

    public FeeValidator(CustomerInfo customerInfo) {
        this.customerInfo = customerInfo;
    }

    @Override
    public void validate() {
        if (LocalDate.now().isBefore(customerInfo.getFeeExpiry()))
            super.validate();
        else throw new ValidatorException("Registration fee is expired");
    }
}
