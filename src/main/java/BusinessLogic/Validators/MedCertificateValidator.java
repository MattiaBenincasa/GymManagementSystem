package BusinessLogic.Validators;

import BusinessLogic.DTOs.CustomerInfoDTO;
import BusinessLogic.Exceptions.ValidatorException;

import java.time.LocalDate;

public class MedCertificateValidator extends Validator{
    private CustomerInfoDTO customerInfoDTO;

    public MedCertificateValidator(CustomerInfoDTO customerInfoDTO) {
        this.customerInfoDTO = customerInfoDTO;
    }

    // FIXME it must manage also the case that the customer does not have the medical certificate
    @Override
    public void Validate() {
        LocalDate expiryDate = this.customerInfoDTO.getCustomer().getMedicalCertificate().getExpiryDate();
        if (expiryDate.plusDays(20).isAfter(LocalDate.now()))
            throw new ValidatorException("Exceeded the maximum time to obtain medical certificate");
        super.Validate();
    }
}
