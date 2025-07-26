package DomainModel.Users;

import java.time.LocalDate;

public class MedicalCertificate {
    private final LocalDate expiryDate;
    private final boolean isCompetitive;

    public MedicalCertificate(LocalDate activationDate, int durationInMonths, boolean isCompetitive) {
        this.expiryDate = activationDate.plusMonths(durationInMonths);
        this.isCompetitive = isCompetitive;
    }

    //medical certificate status
    public boolean isExpired() {
        return LocalDate.now().isEqual(this.expiryDate)||
                LocalDate.now().isAfter(this.expiryDate);
    }

    public boolean isCompetitive() {
        return this.isCompetitive;
    }

}
