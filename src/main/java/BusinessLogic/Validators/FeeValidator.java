package BusinessLogic.Validators;

import BusinessLogic.DTOs.CustomerInfo;
import BusinessLogic.Exceptions.ValidatorException;

import java.time.LocalDate;

public class FeeValidator extends Validator{

    private CustomerInfo customerInfo;

    public FeeValidator(CustomerInfo customerInfo) {
        this.customerInfo = customerInfo;
    }

    @Override
    public void Validate() {
        if (LocalDate.now().isBefore(customerInfo.getFeeExpiry()))
            super.Validate();
        else throw new ValidatorException("Registration fee is expired");
    }
}
