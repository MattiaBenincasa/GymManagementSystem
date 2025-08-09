package ORM.Membership;

import BusinessLogic.Exceptions.DAOException;
import DomainModel.DiscountStrategy.DiscountStrategy;
import DomainModel.Membership.CourseMembership;
import DomainModel.Membership.Membership;
import DomainModel.Membership.WeightRoomMembership;
import ORM.ConnectionManager;
import ORM.DiscountStrategy.DiscountsDAO;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MembershipDAO {
    private final Connection connection;
    private final DiscountsDAO discountsDAO;

    public MembershipDAO(DiscountsDAO discountsDAO) {
        this.connection = ConnectionManager.getSingleInstance().getConnection();
        this.discountsDAO = discountsDAO;
    }

    public int createMembership(Membership membership) {
        String sql = "INSERT INTO Membership (name, description, durationInDays, price) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, membership.getName());
            statement.setString(2, membership.getDescription());
            statement.setInt(3, membership.getDurationInDays());
            statement.setBigDecimal(4, membership.getPrice());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new DAOException("Creating membership failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error during INSERT into Membership: " + e.getMessage(), e);
        }
    }

    public void deleteMembership(int membershipId) {
        String sql = "DELETE FROM Membership WHERE id = ?";

        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setInt(1, membershipId);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Deleting membership failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DAOException("Error during DELETE from Membership: " + e.getMessage(), e);
        }
    }

    public void updateMembership(Membership membership) {
        String sql = "UPDATE Membership SET name = ?, description = ?, durationInDays = ?, price = ? WHERE id = ?";

        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setString(1, membership.getName());
            statement.setString(2, membership.getDescription());
            statement.setInt(3, membership.getDurationInDays());
            statement.setBigDecimal(4, membership.getPrice());
            statement.setInt(5, membership.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Updating membership failed, no rows affected.");
            }

            updateDiscountsForMembership(membership.getId(), membership.getDiscounts());

        } catch (SQLException e) {
            throw new DAOException("Error during UPDATE of Membership: " + e.getMessage(), e);
        }
    }

    private void updateDiscountsForMembership(int membershipId, List<DiscountStrategy> discounts) throws DAOException {
        deleteDiscountsForMembership(membershipId);
        addDiscountsToMembership(membershipId, discounts);
    }

    private void deleteDiscountsForMembership(int membershipId) throws DAOException {
        String sql = "DELETE FROM MembershipDiscounts WHERE membership_id = ?";
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setInt(1, membershipId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error during DELETE from MembershipDiscounts: " + e.getMessage(), e);
        }
    }

    private void addDiscountsToMembership(int membershipId, List<DiscountStrategy> discounts) throws DAOException {
        if (discounts == null || discounts.isEmpty()) return;

        String sql = "INSERT INTO MembershipDiscounts (membership_id, discount_id) VALUES (?, ?)";
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            for (DiscountStrategy discount : discounts) {
                statement.setInt(1, membershipId);
                statement.setInt(2, discount.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new DAOException("Error during batch INSERT into MembershipDiscounts: " + e.getMessage(), e);
        }
    }

    public Membership getMembershipByID(int membershipID) throws DAOException {
        String sql = "SELECT m.id, m.name, m.description, m.durationInDays, m.price, " +
                "cm.id as courseMembershipId, cm.weeklyAccess, cm.course as courseId, " +
                "wrm.id as wrMembershipId, wrm.type as wrMembershipType " +
                "FROM Membership m " +
                "LEFT JOIN CourseMembership cm ON m.id = cm.id " +
                "LEFT JOIN WRMembership wrm ON m.id = wrm.id " +
                "WHERE m.id = ?";

        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setInt(1, membershipID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    int duration = resultSet.getInt("durationInDays");
                    BigDecimal price = resultSet.getBigDecimal("price");

                    Membership membership;

                    int courseMembershipId = resultSet.getInt("courseMembershipId");
                    int wrMembershipId = resultSet.getInt("wrMembershipId");

                    if (courseMembershipId != 0) {
                        membership = new CourseMembership(id);
                    } else {
                        membership = new WeightRoomMembership(id);
                    }
                    membership.setName(name);
                    membership.setDescription(description);
                    membership.setDurationInDays(duration);
                    membership.setPrice(price);
                    return membership;
                } else {
                    throw new DAOException("Membership with ID " + membershipID + " not found.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error during SELECT from Membership tables: " + e.getMessage(), e);
        }
    }

    public List<DiscountStrategy> getDiscountsForMembership(int membershipId) throws DAOException {
        List<DiscountStrategy> discounts = new ArrayList<>();
        String sql = "SELECT discount_id FROM MembershipDiscounts WHERE membership_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, membershipId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int discountId = resultSet.getInt("discount_id");
                    DiscountStrategy discount = this.discountsDAO.getDiscountByID(discountId);
                    discounts.add(discount);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error getting discounts for membership: " + e.getMessage(), e);
        }
        return discounts;
    }

}