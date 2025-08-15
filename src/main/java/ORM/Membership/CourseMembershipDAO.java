package ORM.Membership;

import Exceptions.DAOException;
import DomainModel.DiscountStrategy.DiscountStrategy;
import DomainModel.Membership.CourseMembership;
import DomainModel.Membership.Course;
import ORM.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseMembershipDAO {
    private final Connection connection;
    private final MembershipDAO membershipDAO;
    private final CourseDAO courseDAO;

    public CourseMembershipDAO(MembershipDAO membershipDAO, CourseDAO courseDAO) {
        this.connection = ConnectionManager.getSingleInstance().getConnection();
        this.membershipDAO = membershipDAO;
        this.courseDAO = courseDAO;
    }

    public CourseMembership createCourseMembership(CourseMembership courseMembership) {
        int membershipId = membershipDAO.createMembership(courseMembership);

        String sql = "INSERT INTO CourseMembership (id, course, weeklyAccess) VALUES (?, ?, ?)";

        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setInt(1, membershipId);
            if (courseMembership.getCourse() != null) {
                statement.setInt(2, courseMembership.getCourse().getId());
            } else {
                statement.setNull(2, java.sql.Types.INTEGER);
            }
            statement.setInt(3, courseMembership.getWeeklyAccess());

            statement.executeUpdate();
            return new CourseMembership(membershipId, courseMembership);
        } catch (SQLException e) {
            throw new DAOException("Error during INSERT into CourseMembership: " + e.getMessage(), e);
        }
    }

    public CourseMembership getCourseMembershipByID(int membershipID) {
        String sql = "SELECT m.id, m.name, m.description, m.durationInDays, m.price, cm.course, cm.weeklyAccess " +
                "FROM Membership m JOIN CourseMembership cm ON m.id = cm.id WHERE m.id = ?";

        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setInt(1, membershipID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    CourseMembership courseMembership = new CourseMembership(resultSet.getInt("id"));
                    courseMembership.setName(resultSet.getString("name"));
                    courseMembership.setDescription(resultSet.getString("description"));
                    courseMembership.setDurationInDays(resultSet.getInt("durationInDays"));
                    courseMembership.setPrice(resultSet.getBigDecimal("price"));
                    int courseId = resultSet.getInt("course");
                    if (!resultSet.wasNull()) {
                        Course course = courseDAO.getCourseByID(courseId);
                        courseMembership.setCourse(course);
                    }
                    courseMembership.setWeeklyAccess(resultSet.getInt("weeklyAccess"));
                    List<DiscountStrategy> discounts = this.membershipDAO.getDiscountsForMembership(membershipID);
                    for (DiscountStrategy discountStrategy : discounts)
                        courseMembership.addDiscount(discountStrategy);

                    return courseMembership;
                } else {
                    throw new DAOException("CourseMembership with ID " + membershipID + " not found.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error during SELECT from CourseMembership: " + e.getMessage(), e);
        }
    }

    public void updateCourseMembership(CourseMembership courseMembership) {
        membershipDAO.updateMembership(courseMembership);

        String sql = "UPDATE CourseMembership SET course = ?, weeklyAccess = ? WHERE id = ?";

        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            if (courseMembership.getCourse() != null) {
                statement.setInt(1, courseMembership.getCourse().getId());
            } else {
                statement.setNull(1, java.sql.Types.INTEGER);
            }
            statement.setInt(2, courseMembership.getWeeklyAccess());
            statement.setInt(3, courseMembership.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Updating CourseMembership failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DAOException("Error during UPDATE of CourseMembership: " + e.getMessage(), e);
        }
    }

    public void deleteCourseMembership(int courseMembershipId) {
        membershipDAO.deleteMembership(courseMembershipId);
    }

    public ArrayList<CourseMembership> getAllCourseMembership() {
        ArrayList<CourseMembership> memberships = new ArrayList<>();
        String sql = "SELECT m.id, m.name, m.description, m.durationInDays, m.price, cm.course, cm.weeklyAccess " +
                "FROM Membership m JOIN CourseMembership cm ON m.id = cm.id";

        try (PreparedStatement statement = this.connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                CourseMembership courseMembership = new CourseMembership(resultSet.getInt("id"));
                courseMembership.setName(resultSet.getString("name"));
                courseMembership.setDescription(resultSet.getString("description"));
                courseMembership.setDurationInDays(resultSet.getInt("durationInDays"));
                courseMembership.setPrice(resultSet.getBigDecimal("price"));

                int courseId = resultSet.getInt("course");
                if (!resultSet.wasNull()) {
                    Course course = courseDAO.getCourseByID(courseId);
                    courseMembership.setCourse(course);
                }
                courseMembership.setWeeklyAccess(resultSet.getInt("weeklyAccess"));
                memberships.add(courseMembership);
            }
        } catch (SQLException e) {
            throw new DAOException("Error during SELECT ALL from CourseMembership: " + e.getMessage(), e);
        }
        return memberships;
    }

}
