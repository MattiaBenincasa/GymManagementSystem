package DomainModel;

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
        return customer;
    }

    public TrainerAvailability getTrainerAvailability() {
        return trainerAvailability;
    }

    public String getAppointmentPurpose() {
        return appointmentPurpose;
    }
}
