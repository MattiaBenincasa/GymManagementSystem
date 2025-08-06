package BusinessLogic.Validators;

import BusinessLogic.DTOs.ClassBookingInfo;
import BusinessLogic.Exceptions.ValidatorException;

public class CheckBookingsValidator extends Validator{
    private ClassBookingInfo classBookingInfo;

    public CheckBookingsValidator (ClassBookingInfo classBookingInfo) {
        this.classBookingInfo = classBookingInfo;
    }

    @Override
    public void validate() {
        if (this.classBookingInfo.getTotalBookingsInClass() >= this.classBookingInfo.getClassMaxParticipants())
            throw new ValidatorException("Class is full");
        if (this.classBookingInfo.getBookingsDoneByCustomerThisWeek() >= this.classBookingInfo.getWeeklyBookingLimit())
            throw  new ValidatorException("You have exceeded your weekly booking allowed");
        super.validate();
    }
}
