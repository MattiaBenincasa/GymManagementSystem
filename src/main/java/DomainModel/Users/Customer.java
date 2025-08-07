package DomainModel.Users;

public class Customer extends User {
    private MedicalCertificate medicalCertificate;
    private CustomerCategory customerCategory;

    public Customer() {}

    public Customer(int id, Customer customer) {
        super(id, customer);
        this.medicalCertificate = customer.medicalCertificate;
        this.customerCategory = customer.customerCategory;
    }

    public Customer(Customer customer) {
        super(customer);
        this.medicalCertificate = customer.medicalCertificate;
        this.customerCategory = customer.customerCategory;
    }

    public MedicalCertificate getMedicalCertificate() {
        return medicalCertificate;
    }

    public void setMedicalCertificate(MedicalCertificate medicalCertificate) {
        this.medicalCertificate = medicalCertificate;
    }

    public CustomerCategory getCustomerCategory() {
        return customerCategory;
    }

    public void setCustomerCategory(CustomerCategory customerCategory) {
        if (!customerCategory.isValidFor(this))
            throw new IllegalStateException("This category is not applicable to this customer");
        this.customerCategory = customerCategory;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj))
            return false;

        Customer customer = (Customer) obj;

        boolean medicalCertificateEquals;

        if (medicalCertificate != null)
            medicalCertificateEquals = medicalCertificate.equals(customer.medicalCertificate);
        else
            medicalCertificateEquals = (medicalCertificate==customer.getMedicalCertificate());

        return medicalCertificateEquals &&
                customerCategory == customer.customerCategory;
    }
}
