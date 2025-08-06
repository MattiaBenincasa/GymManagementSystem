package BusinessLogic.Validators;

import BusinessLogic.DTOs.WeightRoomBookingInfo;
import BusinessLogic.Exceptions.ValidatorException;
import DomainModel.Membership.WRMembershipType;

public class WeightRoomBookingValidator extends Validator {
    private final WeightRoomBookingInfo weightRoomBookingInfo;

    public WeightRoomBookingValidator(WeightRoomBookingInfo weightRoomBookingInfo) {
        this.weightRoomBookingInfo = weightRoomBookingInfo;
    }

    @Override
    public void validate() {
        if (weightRoomBookingInfo.getMembershipType() == WRMembershipType.BASE &&
        weightRoomBookingInfo.getCustomerMonthlyAppointmentsDone() >=1)
            throw new ValidatorException("Monthly appointment limit reached for BASE membership.");

        super.validate();
    }
}
