package BusinessLogic.Validators;

import DomainModel.Users.Customer;

public class BasicValidator {

    public void checkMedicalCertificate(Customer customer) {}

    public void checkRegistrationFee(Customer customer) {
        // get all registration fee from db with DAO
        // and check if one of them is valid. A registrationFee
        // can be invalid if is expired or is not activated yet
    }

}
