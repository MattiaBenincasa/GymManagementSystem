package DomainModel.Users;

import java.time.LocalDate;

public class MedicalCertificate {
    private final LocalDate expiryDate;
    private final boolean isCompetitive;

    public MedicalCertificate(LocalDate expiryDate, boolean isCompetitive) {
        this.expiryDate = expiryDate;
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
