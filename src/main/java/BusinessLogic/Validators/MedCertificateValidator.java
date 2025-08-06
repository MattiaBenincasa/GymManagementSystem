package BusinessLogic.Validators;

import BusinessLogic.DTOs.CustomerInfo;
import BusinessLogic.Exceptions.ValidatorException;
import DomainModel.Users.MedicalCertificate;

import java.time.LocalDate;

public class MedCertificateValidator extends Validator{
    private CustomerInfo customerInfo;

    public MedCertificateValidator(CustomerInfo customerInfoDTO) {
        this.customerInfo = customerInfoDTO;
    }

    @Override
    public void Validate() {

        LocalDate firstDeadline = this.customerInfo.getFeeBegin().plusDays(20);

        if (this.customerInfo.getMedCertExpiry() == null) {
            if (LocalDate.now().isBefore(firstDeadline))
                super.Validate();
            else throw new ValidatorException("Exceeded the maximum time to obtain medical certificate");
            // new customer passed the deadline for bringing the first medical certificate
        } else {
            LocalDate deadline = this.customerInfo.getMedCertExpiry().plusDays(20);
            if (LocalDate.now().isBefore(deadline))
                super.Validate();
            else throw new ValidatorException("Exceeded the maximum time to bring new medical certificate");
            // Customer passed the deadline for bringing new medical certificate
        }
    }
}
