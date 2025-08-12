package Controllers;

import BusinessLogic.DailyEvents.DailyClassService;
import BusinessLogic.Memberships.CourseMembershipService;
import BusinessLogic.Memberships.CourseService;
import BusinessLogic.Memberships.WRMembershipService;


public class AdminController {
    private final CourseMembershipService courseMembershipService;
    private final WRMembershipService wrMembershipService;
    private final CourseService courseService;
    private final DailyClassService dailyClassService;
}
