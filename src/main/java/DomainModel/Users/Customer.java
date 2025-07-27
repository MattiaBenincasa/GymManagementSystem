package DomainModel.Users;

import java.time.LocalDate;

public class Customer extends User {
    private Gender gender;
    private LocalDate registrationFeeExpiryDate;
    private LocalDate insuranceFeeExpiryDate;
    private MedicalCertificate medicalCertificate;

    public Customer() {}

    public Customer(int id) {
        super(id);
    }

    public Customer(Customer customer) {
        super(customer);
        this.gender = customer.gender;
        this.registrationFeeExpiryDate = customer.registrationFeeExpiryDate;
        this.insuranceFeeExpiryDate = customer.insuranceFeeExpiryDate;
        this.medicalCertificate = customer.medicalCertificate;
    }

    public boolean registrationFeeIsExpired() {
        return LocalDate.now().isEqual(this.registrationFeeExpiryDate)||
                LocalDate.now().isAfter(this.registrationFeeExpiryDate);
    }

    public boolean insuranceFeeIsExpired() {
        return LocalDate.now().isEqual(this.insuranceFeeExpiryDate)||
                LocalDate.now().isAfter(this.insuranceFeeExpiryDate);
    }

    public void setRegistrationFeeExpiryDate(LocalDate expiryDate) {
        if (!registrationFeeIsExpired())
            throw new IllegalStateException("You cannot change registrationFeeExpiryDate: it has not expired yet.");
        this.registrationFeeExpiryDate = expiryDate;
    }

    public void setInsuranceFeeExpiryDate(LocalDate expiryDate) {
        if (!insuranceFeeIsExpired())
            throw new IllegalStateException("You cannot change insuranceFeeExpiryDate: it has not expired yet.");
        this.insuranceFeeExpiryDate = expiryDate;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Gender getGender() {
        return this.gender;
    }

    public MedicalCertificate getMedicalCertificate() {
        return medicalCertificate;
    }

    public void setMedicalCertificate(MedicalCertificate medicalCertificate) {
        this.medicalCertificate = medicalCertificate;
    }
}
