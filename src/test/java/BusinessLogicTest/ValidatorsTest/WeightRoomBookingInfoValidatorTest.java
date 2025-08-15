package BusinessLogicTest.ValidatorsTest;

import BusinessLogic.DTOs.WeightRoomBookingInfo;
import Exceptions.ValidatorException;
import BusinessLogic.Validators.MembershipValidator;
import BusinessLogic.Validators.Validator;
import BusinessLogic.Validators.WeightRoomBookingValidator;
import DomainModel.Membership.WRMembershipType;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WeightRoomBookingInfoValidatorTest {

    @Test
    void testInvalid_ExpiredMembership() {
        WeightRoomBookingInfo info = new WeightRoomBookingInfo(
                LocalDate.now(),
                WRMembershipType.BASE,
                0);

        Validator validatorChain = new MembershipValidator(info.getMembershipExpiry());

        assertThrows(ValidatorException.class, validatorChain::validate);
    }

    @Test
    void testInvalid_BaseMembershipAndMonthlyAppointmentLimitReached() {
        WeightRoomBookingInfo info = new WeightRoomBookingInfo(
                LocalDate.now().plusMonths(6),
                WRMembershipType.BASE,
                1);

        Validator validatorChain = new MembershipValidator(info.getMembershipExpiry())
                .setNextValidator(new WeightRoomBookingValidator(info));

        assertThrows(ValidatorException.class, validatorChain::validate);
    }


    @Test
    void testValid_BaseMembershipWithNoAppointments() {
        WeightRoomBookingInfo info = new WeightRoomBookingInfo(
                LocalDate.now().plusMonths(6),
                WRMembershipType.BASE,
                0);

        Validator validatorChain = new MembershipValidator(info.getMembershipExpiry())
                .setNextValidator(new WeightRoomBookingValidator(info));

        assertDoesNotThrow(validatorChain::validate);
    }

    @Test
    void testValid_PersonalMembership() {
        WeightRoomBookingInfo info = new WeightRoomBookingInfo(
                LocalDate.now().plusMonths(6),
                WRMembershipType.PERSONAL,
                5);

        Validator validatorChain = new MembershipValidator(info.getMembershipExpiry())
                .setNextValidator(new WeightRoomBookingValidator(info));

        assertDoesNotThrow(validatorChain::validate);
    }
}
