package ORM.Membership;

import BusinessLogic.Exceptions.DAOException;
import DomainModel.Membership.WRMembershipType;
import DomainModel.Membership.WeightRoomMembership;
import ORM.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WeightRoomMembershipDAO {
    private Connection connection;
    private MembershipDAO membershipDAO;

    public WeightRoomMembershipDAO(MembershipDAO membershipDAO) {
        this.connection = ConnectionManager.getSingleInstance().getConnection();
        this.membershipDAO = membershipDAO;
    }

    public int createWeightRoomMembership(WeightRoomMembership wrMembership) {
        int membershipId = membershipDAO.createMembership(wrMembership);

        String sql = "INSERT INTO WRMembership (id, type) VALUES (?, ?)";

        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setInt(1, membershipId);
            statement.setString(2, wrMembership.getType().name());

            statement.executeUpdate();
            return membershipId;
        } catch (SQLException e) {
            throw new DAOException("Error during INSERT into WRMembership: " + e.getMessage(), e);
        }
    }

    public WeightRoomMembership getWeightRoomMembershipByID(int membershipID) {
        String sql = "SELECT m.id, m.name, m.description, m.durationInDays, m.price, wr.type " +
                "FROM Membership m JOIN WRMembership wr ON m.id = wr.id WHERE m.id = ?";

        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setInt(1, membershipID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    WeightRoomMembership wrMembership = new WeightRoomMembership(resultSet.getInt("id"));
                    wrMembership.setName(resultSet.getString("name"));
                    wrMembership.setDescription(resultSet.getString("description"));
                    wrMembership.setDurationInDays(resultSet.getInt("durationInDays"));
                    wrMembership.setPrice(resultSet.getBigDecimal("price"));
                    wrMembership.setType(WRMembershipType.valueOf(resultSet.getString("type")));
                    return wrMembership;
                } else {
                    throw new DAOException("WRMembership with ID " + membershipID + " not found.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error during SELECT from WRMembership: " + e.getMessage(), e);
        }
    }


    public void updateWeightRoomMembership(WeightRoomMembership wrMembership) {
        membershipDAO.updateMembership(wrMembership);

        String sql = "UPDATE WRMembership SET type = ? WHERE id = ?";

        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setString(1, wrMembership.getType().name());
            statement.setInt(2, wrMembership.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Updating WRMembership failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DAOException("Error during UPDATE of WRMembership: " + e.getMessage(), e);
        }
    }

    public void deleteWRMembership(int wrMembershipID) {
        membershipDAO.deleteMembership(wrMembershipID);
    }

}
