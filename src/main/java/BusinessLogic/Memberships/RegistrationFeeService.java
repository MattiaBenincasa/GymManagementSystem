package BusinessLogic.Memberships;

import DomainModel.Membership.RegistrationFee;
import ORM.Membership.RegistrationFeeDAO;

import java.math.BigDecimal;

public class RegistrationFeeService {
    private final RegistrationFeeDAO registrationFeeDAO;

    public RegistrationFeeService(RegistrationFeeDAO registrationFeeDAO) {
        this.registrationFeeDAO = registrationFeeDAO;
    }

    public void updateRegistrationFee(BigDecimal price, String description) {
        RegistrationFee.getRegistrationFee().setPrice(price);
        RegistrationFee.getRegistrationFee().setDescription(description);
        if (this.registrationFeeDAO.getRegistrationFee().isPresent())
            registrationFeeDAO.updateRegistrationFee(RegistrationFee.getRegistrationFee());
        else registrationFeeDAO.createRegistrationFee(RegistrationFee.getRegistrationFee());
    }


}
