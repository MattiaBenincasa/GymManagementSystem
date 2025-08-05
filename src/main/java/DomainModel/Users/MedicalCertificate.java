package DomainModel.Users;

import java.time.LocalDate;

public class MedicalCertificate {
    private final LocalDate expiryDate;
    private final boolean isCompetitive;

    public MedicalCertificate(LocalDate activationDate, int durationInMonths, boolean isCompetitive) {
        this.expiryDate = activationDate.plusMonths(durationInMonths);
        this.isCompetitive = isCompetitive;
    }

    public MedicalCertificate(LocalDate expiryDate, boolean isCompetitive) {
        this.expiryDate = expiryDate;
        this.isCompetitive = isCompetitive;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    //medical certificate status
    public boolean isExpired() {
        return LocalDate.now().isEqual(this.expiryDate)||
                LocalDate.now().isAfter(this.expiryDate);
    }

    public boolean isCompetitive() {
        return this.isCompetitive;
    }

    @Override
    public boolean equals(Object obj) {
        if (this==obj)
            return true;

        if (obj == null || getClass() !=obj.getClass())
            return false;

        MedicalCertificate medicalCertificate = (MedicalCertificate) obj;

        return expiryDate.equals(medicalCertificate.expiryDate) &&
                isCompetitive == medicalCertificate.isCompetitive();
    }
}
