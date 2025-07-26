package DomainModelTests;
import DomainModel.Users.MedicalCertificate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

public class MedicalCertificateTest {

    @Test
    void medCertificateShouldExpired() {
        LocalDate pastDay = LocalDate.of(2021, Month.FEBRUARY, 18);
        LocalDate aYearAgoToday = LocalDate.now().minusMonths(12);

        MedicalCertificate pastDayMedCertificate = new MedicalCertificate(pastDay, 12, false);
        MedicalCertificate medCertificateExpiredToday = new MedicalCertificate(aYearAgoToday, 12, false);

        Assertions.assertTrue(pastDayMedCertificate.isExpired());
        Assertions.assertTrue(medCertificateExpiredToday.isExpired());
    }

    @Test
    void medCertificateShouldNotExpired() {
        LocalDate today = LocalDate.now();
        LocalDate aYearAgoTodayPlusOne = LocalDate.now().minusMonths(12).plusDays(1);

        MedicalCertificate medCertActivatedToday = new MedicalCertificate(today, 12, true);
        MedicalCertificate medCertIsAboutToExpire = new MedicalCertificate(aYearAgoTodayPlusOne, 12, true);

        Assertions.assertFalse(medCertActivatedToday.isExpired());
        Assertions.assertFalse(medCertIsAboutToExpire.isExpired());
    }

}
