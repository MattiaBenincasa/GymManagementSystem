package BusinessLogic.Validators;

import DomainModel.Users.Customer;

public abstract class Validator {
    protected Validator nextValidator;

    public Validator setNextValidator(Validator nextValidator) {
        this.nextValidator = nextValidator;
        return nextValidator;
    }

    public void Validate() {
        if (this.nextValidator != null)
            this.nextValidator.Validate();
    }

}
