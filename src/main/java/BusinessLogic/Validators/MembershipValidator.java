package BusinessLogic.Validators;

import BusinessLogic.DTOs.ClassBookingInfo;
import BusinessLogic.Exceptions.ValidatorException;
import DomainModel.Membership.CustomerMembership;

import java.time.LocalDate;

public class MembershipValidator extends Validator{
    private ClassBookingInfo classBookingInfo;

    public MembershipValidator(ClassBookingInfo classBookingInfo) {
        this.classBookingInfo = classBookingInfo;
    }

    @Override
    public void Validate() {
        if (this.classBookingInfo.getMembershipExpiry() != null) {
            LocalDate deadline = this.classBookingInfo.getMembershipExpiry();
            if (LocalDate.now().isBefore(deadline))
                super.Validate();
            else throw new ValidatorException("Membership has expired");
        } else {
            throw new ValidatorException("You do not have the necessary subscription to book this class");
        }
    }
}
