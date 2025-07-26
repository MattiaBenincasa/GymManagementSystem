package DomainModel.Users;

public class Staff extends User{
    private final StaffRole staffRole;

    public Staff(StaffRole staffRole) {
        this.staffRole = staffRole;
    }

    public Staff(int id, StaffRole staffRole) {
        super(id);
        this.staffRole = staffRole;
    }

    public StaffRole getStaffRole() {
        return this.staffRole;
    }
}
