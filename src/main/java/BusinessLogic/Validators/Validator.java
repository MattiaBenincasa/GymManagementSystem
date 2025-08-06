package BusinessLogic.Validators;

public abstract class Validator {
    protected Validator nextValidator;

    public Validator setNextValidator(Validator nextValidator) {
        this.nextValidator = nextValidator;
        return nextValidator;
    }

    public void validate() {
        if (this.nextValidator != null)
            this.nextValidator.validate();
    }

}
