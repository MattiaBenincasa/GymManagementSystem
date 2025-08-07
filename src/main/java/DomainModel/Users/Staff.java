package DomainModel.Users;

public class Staff extends User{
    private final StaffRole staffRole;

    public Staff(StaffRole staffRole) {
        this.staffRole = staffRole;
    }

    public Staff(int id, Staff staff) {
        super(id, staff);
        this.staffRole = staff.staffRole;
    }

    public StaffRole getStaffRole() {
        return this.staffRole;
    }
}
