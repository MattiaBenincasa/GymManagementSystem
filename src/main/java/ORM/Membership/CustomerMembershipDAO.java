package ORM.Membership;

import BusinessLogic.Exceptions.DAOException;
import DomainModel.Membership.CourseMembership;
import DomainModel.Membership.Membership;
import DomainModel.Membership.WeightRoomMembership;
import DomainModel.Membership.CustomerMembership;
import DomainModel.Users.Customer;
import ORM.ConnectionManager;
import ORM.Membership.CourseMembershipDAO;
import ORM.Membership.WeightRoomMembershipDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CustomerMembershipDAO {
    private Connection connection;
    private WeightRoomMembershipDAO wrMembershipDAO;
    private CourseMembershipDAO courseMembershipDAO;

    public CustomerMembershipDAO(CourseDAO courseDAO, MembershipDAO membershipDAO) {
        this.connection = ConnectionManager.getSingleInstance().getConnection();
        this.wrMembershipDAO = new WeightRoomMembershipDAO(membershipDAO);
        this.courseMembershipDAO = new CourseMembershipDAO(membershipDAO,courseDAO);
    }

    public void createCustomerMembership(CustomerMembership customerMembership) {
        String sql = "INSERT INTO CustomerMembership (customer_id, membership_id, begindate, expdate) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setInt(1, customerMembership.getCustomer().getId());
            statement.setInt(2, customerMembership.getMembership().getId());
            statement.setDate(3, java.sql.Date.valueOf(customerMembership.getStartDay()));
            statement.setDate(4, java.sql.Date.valueOf(customerMembership.getExpiryDate()));

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Creating customer membership failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DAOException("Error during INSERT into CustomerMembership: " + e.getMessage(), e);
        }
    }

    public ArrayList<CustomerMembership> getAllCustomerMembership(Customer customer) {
        ArrayList<CustomerMembership> customerMemberships = new ArrayList<>();
        String sql = "SELECT membership_id, begindate, expdate FROM CustomerMembership WHERE customer_id = ?";

        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setInt(1, customer.getId());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int membershipId = resultSet.getInt("membership_id");
                    LocalDate startDate = resultSet.getDate("begindate").toLocalDate();
                    LocalDate expiryDate = resultSet.getDate("expdate").toLocalDate();
                    Membership membership;

                    try {
                        membership = wrMembershipDAO.getWeightRoomMembershipByID(membershipId);
                    } catch (DAOException e) {
                        try {
                            membership = courseMembershipDAO.getCourseMembershipByID(membershipId);
                        } catch (DAOException ex) {
                            throw new DAOException("Membership with ID " + membershipId + " not found or is of an invalid type.");
                        }
                    }

                    customerMemberships.add(new CustomerMembership(customer, membership, startDate, expiryDate));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error during SELECT from CustomerMembership: " + e.getMessage(), e);
        }
        return customerMemberships;
    }
}