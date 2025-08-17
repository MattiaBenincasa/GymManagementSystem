package DTOs;

import java.time.LocalDate;

public class ClassBookingInfo {

    private final LocalDate membershipExpiry;
    private final int classMaxParticipants;
    private final int totalBookingsInClass;
    private final int weeklyBookingLimit;
    private final int bookingsDoneByCustomerThisWeek;

    private ClassBookingInfo(Builder builder) {
        this.membershipExpiry = builder.membershipExpiry;
        this.classMaxParticipants = builder.classMaxParticipants;
        this.totalBookingsInClass = builder.totalBookingsInClass;
        this.weeklyBookingLimit = builder.weeklyBookingLimit;
        this.bookingsDoneByCustomerThisWeek = builder.bookingsDoneByCustomerThisWeek;
    }

    public static class Builder {
        private LocalDate membershipExpiry;
        private int classMaxParticipants;
        private int totalBookingsInClass;
        private int weeklyBookingLimit;
        private int bookingsDoneByCustomerThisWeek;

        public Builder membershipExpiry(LocalDate membershipExpiry) {
            this.membershipExpiry = membershipExpiry;
            return this;
        }

        public Builder classMaxParticipants(int classMaxParticipants) {
            this.classMaxParticipants = classMaxParticipants;
            return this;
        }

        public Builder totalBookingsInClass(int totalBookingsInClass) {
            this.totalBookingsInClass = totalBookingsInClass;
            return this;
        }

        public Builder weeklyBookingLimit(int weeklyBookingLimit) {
            this.weeklyBookingLimit = weeklyBookingLimit;
            return this;
        }

        public Builder bookingsDoneByCustomerThisWeek(int bookingsDoneByCustomerThisWeek) {
            this.bookingsDoneByCustomerThisWeek = bookingsDoneByCustomerThisWeek;
            return this;
        }

        public ClassBookingInfo build() {
            return new ClassBookingInfo(this);
        }
    }

    // Getters

    public LocalDate getMembershipExpiry() {
        return membershipExpiry;
    }

    public int getClassMaxParticipants() {
        return classMaxParticipants;
    }

    public int getTotalBookingsInClass() {
        return totalBookingsInClass;
    }

    public int getWeeklyBookingLimit() {
        return weeklyBookingLimit;
    }

    public int getBookingsDoneByCustomerThisWeek() {
        return bookingsDoneByCustomerThisWeek;
    }
}

