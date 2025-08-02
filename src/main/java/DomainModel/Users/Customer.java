package DomainModel.Users;

public class Customer extends User {
    private MedicalCertificate medicalCertificate;

    public Customer() {}

    public Customer(int id) {
        super(id);
    }

    public Customer(Customer customer) {
        super(customer);
        this.medicalCertificate = customer.medicalCertificate;
    }

    public MedicalCertificate getMedicalCertificate() {
        return medicalCertificate;
    }

    public void setMedicalCertificate(MedicalCertificate medicalCertificate) {
        this.medicalCertificate = medicalCertificate;
    }
}
