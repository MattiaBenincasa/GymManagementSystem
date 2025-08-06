package BusinessLogic.DTOs;

import java.time.LocalDate;

public class CustomerInfo {
    private final LocalDate medCertExpiry;
    private final LocalDate feeBegin;
    private final LocalDate feeExpiry;

    public CustomerInfo(LocalDate medCertExpiry, LocalDate feeBegin, LocalDate feeExpiry) {
        this.medCertExpiry = medCertExpiry;
        this.feeBegin = feeBegin;
        this.feeExpiry = feeExpiry;
    }

    public LocalDate getMedCertExpiry() {
        return medCertExpiry;
    }

    public LocalDate getFeeBegin() {
        return feeBegin;
    }

    public LocalDate getFeeExpiry() {
        return feeExpiry;
    }
}
