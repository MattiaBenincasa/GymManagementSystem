package DomainModel.Membership;

import DomainModel.Course;

public class CourseMembership extends Membership{
    private int weeklyAccess;
    private Course course;

    public CourseMembership() {}

    public CourseMembership(int id) {
        super(id);
    }

    public CourseMembership(CourseMembership courseMembership) {
        super(courseMembership);
        this.course = courseMembership.course;
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

    public Course getCourse() {
        return new Course(course);
    }

    public void setCourse(Course course) {
        this.course = new Course(course);
    }
}
