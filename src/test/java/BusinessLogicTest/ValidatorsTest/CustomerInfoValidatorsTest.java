package BusinessLogicTest.ValidatorsTest;

import BusinessLogic.DTOs.CustomerInfo;
import BusinessLogic.Exceptions.ValidatorException;
import BusinessLogic.Validators.FeeValidator;
import BusinessLogic.Validators.MedCertificateValidator;
import BusinessLogic.Validators.Validator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

public class CustomerInfoValidatorsTest {

    @Test
    void testInvalidMedicalCertificate() {
        // with subscription but without medical certificate after 20 days from subscription
        LocalDate twentyDaysBefore = LocalDate.now().minusDays(20);
        LocalDate expiryDate = twentyDaysBefore.plusYears(1);
        CustomerInfo customerInfo = new CustomerInfo(
                null, twentyDaysBefore, expiryDate);
        MedCertificateValidator medCertificateValidator = new MedCertificateValidator(customerInfo);

        assertThrows(ValidatorException.class, medCertificateValidator::validate);

        //with medical certificate but expired 20 days ago

        LocalDate medCertExpDate = LocalDate.now().minusDays(20);
        LocalDate beginFee = LocalDate.now().minusDays(50);
        LocalDate endFee = beginFee.plusYears(1);
        CustomerInfo newCustomerInfo = new CustomerInfo(medCertExpDate, beginFee, endFee);

        MedCertificateValidator newMedCertValidator = new MedCertificateValidator(newCustomerInfo);
        assertThrows(ValidatorException.class, newMedCertValidator::validate);
    }

    @Test
    void testInvalidRegistrationFee() {
        //registration fee expiry today

        CustomerInfo customerInfo = new CustomerInfo(LocalDate.now().plusYears(1),
                LocalDate.now().minusYears(1),
                LocalDate.now());
        FeeValidator feeValidator = new FeeValidator(customerInfo);

        assertThrows(ValidatorException.class, feeValidator::validate);

    }

    @Test
    void testValidUserInformation() {
        //no med cert but sub 19 days ago
        LocalDate feeBegin_1 = LocalDate.now().minusDays(19);
        LocalDate feeExpiry_1 = feeBegin_1.plusYears(1);
        CustomerInfo customerInfo_1 = new CustomerInfo(null, feeBegin_1, feeExpiry_1);

        Validator firstValidator = new FeeValidator(customerInfo_1)
                .setNextValidator(new MedCertificateValidator(customerInfo_1));

        assertDoesNotThrow(firstValidator::validate);

        //with med. cert. but it is expired 19 days ago
        LocalDate medCertExpiry_2 = LocalDate.now().minusDays(10);
        LocalDate feeBegin_2 = LocalDate.now().minusDays(50);
        LocalDate feeExpiry_2 = feeBegin_1.plusYears(1);
        CustomerInfo customerInfo_2 = new CustomerInfo(medCertExpiry_2, feeBegin_2, feeExpiry_2);

        Validator secondValidator = new FeeValidator(customerInfo_2)
                .setNextValidator(new MedCertificateValidator(customerInfo_2));

        assertDoesNotThrow(secondValidator::validate);

        //with med. cert. and registration fee
        LocalDate medCertExpiry_3 = LocalDate.now().plusYears(1);
        LocalDate feeBegin_3 = LocalDate.now();
        LocalDate feeExpiry_3 = LocalDate.now().plusYears(1);
        CustomerInfo customerInfo_3 = new CustomerInfo(medCertExpiry_3, feeBegin_3, feeExpiry_3);

        Validator thirdValidator = new FeeValidator(customerInfo_3)
                .setNextValidator(new MedCertificateValidator(customerInfo_3));

        assertDoesNotThrow(thirdValidator::validate);

    }
}
