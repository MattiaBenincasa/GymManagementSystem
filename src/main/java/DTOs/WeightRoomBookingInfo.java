package DTOs;

import DomainModel.Membership.WRMembershipType;

import java.time.LocalDate;


public class WeightRoomBookingInfo {
    private final LocalDate membershipExpiry;
    private final WRMembershipType membershipType;
    private final int customerMonthlyAppointmentsDone;

    public WeightRoomBookingInfo(LocalDate membershipExpiry, WRMembershipType membershipType, int customerMonthlyAppointmentsDone) {
        this.membershipExpiry = membershipExpiry;
        this.membershipType = membershipType;
        this.customerMonthlyAppointmentsDone = customerMonthlyAppointmentsDone;
    }

    public LocalDate getMembershipExpiry() {
        return membershipExpiry;
    }

    public WRMembershipType getMembershipType() {
        return membershipType;
    }

    public int getCustomerMonthlyAppointmentsDone() {
        return customerMonthlyAppointmentsDone;
    }
}
