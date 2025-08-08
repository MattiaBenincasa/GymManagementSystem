package ORM.Membership;

import BusinessLogic.Exceptions.DAOException;
import DomainModel.Membership.RegistrationFee;
import ORM.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class RegistrationFeeDAO {
    private final Connection connection;

    public RegistrationFeeDAO() {
        this.connection = ConnectionManager.getSingleInstance().getConnection();
    }

    public int createRegistrationFee(RegistrationFee fee) throws DAOException {
        if (getRegistrationFee().isPresent()) {
            throw new DAOException("RegistrationFee already exists. Use updateRegistrationFee instead.");
        }
        String sql = "INSERT INTO RegistrationFee (price, description) VALUES (?, ?)";

        try (PreparedStatement statement = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setBigDecimal(1, fee.getPrice());
            statement.setString(2, fee.getDescription());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new DAOException("Creating registration fee failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error during INSERT into RegistrationFee: " + e.getMessage(), e);
        }
    }

    public void updateRegistrationFee(RegistrationFee fee) throws DAOException {
        String sql = "UPDATE RegistrationFee SET price = ?, description = ? WHERE id = ?";

        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setBigDecimal(1, fee.getPrice());
            statement.setString(2, fee.getDescription());
            statement.setInt(3, 1);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Updating registration fee failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DAOException("Error during UPDATE of RegistrationFee: " + e.getMessage(), e);
        }
    }

    public Optional<RegistrationFee> getRegistrationFee() throws DAOException {
        String sql = "SELECT id, price, description FROM RegistrationFee LIMIT 1";

        try (Statement statement = this.connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                RegistrationFee fee = RegistrationFee.getRegistrationFee();
                fee.setPrice(resultSet.getBigDecimal("price"));
                fee.setDescription(resultSet.getString("description"));
                return Optional.of(fee);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DAOException("Error during SELECT from RegistrationFee: " + e.getMessage(), e);
        }
    }
}