package BusinessLogic.Validators;

import Exceptions.ValidatorException;

import java.time.LocalDate;

public class MembershipValidator extends Validator{
    private final LocalDate expiryDate;

    public MembershipValidator(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public void validate() {
        if (this.expiryDate != null) {
            if (LocalDate.now().isBefore(this.expiryDate))
                super.validate();
            else throw new ValidatorException("Membership has expired");
        } else {
            throw new ValidatorException("You do not have the necessary subscription to book this class");
        }
    }
}
