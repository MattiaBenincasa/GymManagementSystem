package DomainModel.Bookings;

import DomainModel.DailyEvents.TrainerAvailability;
import DomainModel.Users.Customer;

public class Appointment {
    private final Customer customer;
    private final TrainerAvailability trainerAvailability;
    private String appointmentPurpose;

    public Appointment(Customer customer, TrainerAvailability trainerAvailability) {
        this.customer = new Customer(customer);
        this.trainerAvailability = new TrainerAvailability(trainerAvailability);
    }

    public void setAppointmentPurpose(String appointmentPurpose) {
        this.appointmentPurpose = appointmentPurpose;
    }

    public Customer getCustomer() {
        return new Customer(customer);
    }

    public TrainerAvailability getTrainerAvailability() {
        return new TrainerAvailability(trainerAvailability);
    }

    public String getAppointmentPurpose() {
        return appointmentPurpose;
    }
}
