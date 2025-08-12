package BusinessLogicTest.ValidatorsTest;

import BusinessLogic.DTOs.ClassBookingInfo;
import BusinessLogic.Exceptions.ValidatorException;
import BusinessLogic.Validators.CheckBookingsValidator;
import BusinessLogic.Validators.MembershipValidator;
import BusinessLogic.Validators.Validator;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ClassBookingInfoValidatorTest {

    @Test
    void testInvalid_ExpiredMembership() {
        // 1. membership expired
        ClassBookingInfo classBookingInfo = new ClassBookingInfo.Builder()
                .membershipExpiry(LocalDate.now())
                .classMaxParticipants(20)
                .totalBookingsInClass(10)
                .weeklyBookingLimit(2)
                .bookingsDoneByCustomerThisWeek(1)
                .build();

        Validator validatorChain = new MembershipValidator(classBookingInfo.getMembershipExpiry());

        assertThrows(ValidatorException.class, validatorChain::validate);
    }

    @Test
    void testInvalid_ClassIsFull() {
        ClassBookingInfo classBookingInfo = new ClassBookingInfo.Builder()
                .membershipExpiry(LocalDate.now().plusYears(1))
                .classMaxParticipants(20)
                .totalBookingsInClass(20)
                .weeklyBookingLimit(2)
                .bookingsDoneByCustomerThisWeek(1)
                .build();

        Validator validatorChain = new CheckBookingsValidator(classBookingInfo);

        assertThrows(ValidatorException.class, validatorChain::validate);
    }

    @Test
    void testInvalid_WeeklyBookingLimitExceeded() {
        ClassBookingInfo classBookingInfo = new ClassBookingInfo.Builder()
                .membershipExpiry(LocalDate.now().plusYears(1))
                .classMaxParticipants(20)
                .totalBookingsInClass(10)
                .weeklyBookingLimit(2)
                .bookingsDoneByCustomerThisWeek(2)
                .build();

        Validator validatorChain = new CheckBookingsValidator(classBookingInfo);

        assertThrows(ValidatorException.class, validatorChain::validate);
    }

    @Test
    void testValid_AllConditionsMet() {

        ClassBookingInfo classBookingInfo = new ClassBookingInfo.Builder()
                .membershipExpiry(LocalDate.now().plusDays(1))
                .classMaxParticipants(20)
                .totalBookingsInClass(19)
                .weeklyBookingLimit(2)
                .bookingsDoneByCustomerThisWeek(1)
                .build();

        Validator validatorChain = new MembershipValidator(classBookingInfo.getMembershipExpiry())
                .setNextValidator(new CheckBookingsValidator(classBookingInfo));

        assertDoesNotThrow(validatorChain::validate);
    }
}
