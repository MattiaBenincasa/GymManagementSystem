package ORM.Membership;

import BusinessLogic.Exceptions.DAOException;
import DomainModel.Membership.Membership;
import ORM.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MembershipDAO {
    private final Connection connection;

    public MembershipDAO() {
        this.connection = ConnectionManager.getSingleInstance().getConnection();
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
        } catch (SQLException e) {
            throw new DAOException("Error during UPDATE of Membership: " + e.getMessage(), e);
        }
    }

    public Membership getMembershipByID(int membershipID) {
        //check if the ID is in CourseMembership -> new CourseMembership
        //or if the ID is in WeightRoomMembership -> new WeightRoomMembership
        return null;
    }
}