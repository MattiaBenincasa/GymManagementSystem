package DomainModel.Membership;

public class CourseMembership extends Membership{
    private int weeklyAccess;

    //TODO add course attribute of Course class

    public CourseMembership() {}

    public CourseMembership(int id) {
        super(id);
    }

    public CourseMembership(CourseMembership courseMembership) {
        super(courseMembership);
        this.weeklyAccess = courseMembership.weeklyAccess;
    }

    public int getWeeklyAccess() {
        return weeklyAccess;
    }

    public void setWeeklyAccess(int weeklyAccess) {
        this.weeklyAccess = weeklyAccess;
    }

    @Override
    public CourseMembership copy() {
        return new CourseMembership(this);
    }
}
