package ORM.DiscountStrategy;

import DomainModel.DiscountStrategy.*;
import DomainModel.Users.CustomerCategory;
import ORM.ConnectionManager;

import BusinessLogic.Exceptions.DAOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.*;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;

public class DiscountsDAO {
    private final Connection connection;

    public DiscountsDAO() {
        this.connection = ConnectionManager.getSingleInstance().getConnection();
    }

    public int createDiscount(DiscountStrategy discount) {
        String sql = "INSERT INTO Discounts (type, value, userCategory) VALUES (?, ?, ?)";
        int discountId;

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, discount.getClass().getSimpleName().toUpperCase());
            statement.setInt(2, discount.getValue());

            if (discount instanceof CustomerBasedDiscount customerDiscount) {
                statement.setString(3, customerDiscount.getCustomerCategory() != null ? customerDiscount.getCustomerCategory().name() : null);
            } else {
                statement.setNull(3, java.sql.Types.VARCHAR);
            }

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Creating discount failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    discountId = generatedKeys.getInt(1);
                } else {
                    throw new DAOException("Creating discount failed, no ID obtained.");
                }

                if (discount instanceof SeasonDiscount seasonalDiscount) {
                    addMonthsToDiscount(discountId, seasonalDiscount.getMonthsWithDiscount());
                }

            }
        } catch (SQLException e) {
            throw new DAOException("Error during INSERT into Discounts: " + e.getMessage(), e);
        }

        return discountId;
    }

    private void addMonthsToDiscount(int discountId, HashSet<Month> months) {
        String sql = "INSERT INTO MonthWithDiscounts (discount_id, month) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Month month : months) {
                statement.setInt(1, discountId);
                statement.setString(2, month.toString());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new DAOException("Error during INSERT into MonthWithDiscounts: " + e.getMessage(), e);
        }
    }

    private HashSet<Month> getMonthsForSeasonalDiscount(int discountId) {
        HashSet<Month> months = new HashSet<>();
        String sql = "SELECT month FROM MonthWithDiscounts WHERE discount_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, discountId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    months.add(Month.valueOf(resultSet.getString("month")));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error getting months for seasonal discount: " + e.getMessage(), e);
        }
        return months;
    }

    public DiscountStrategy getDiscountByID(int id) throws DAOException {
        String sql = "SELECT id, type, description, is_special_offer, value, userCategory FROM Discounts WHERE id = ?";
        DiscountStrategy discount = null;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String type = resultSet.getString("type");
                    String description = resultSet.getString("description");
                    boolean isSpecialOffer = resultSet.getBoolean("is_special_offer");
                    int value = resultSet.getInt("value");
                    String userCategory = resultSet.getString("userCategory");
                    CustomerCategory category = userCategory != null ? CustomerCategory.valueOf(userCategory) : null;

                    discount = switch (type) {
                        case "CUSTOMERBASEDISCOUNT" -> new CustomerBasedDiscount(id, description, isSpecialOffer, category, value);
                        case "PERCENTAGEDISCOUNT" -> new PercentageDiscount(id, description, isSpecialOffer, value);
                        case "FIXEDDISCOUNT" -> new FixedDiscount(id, description, isSpecialOffer, value);
                        case "SEASONDISCOUNT" -> {
                            HashSet<Month> months = getMonthsForSeasonalDiscount(id);
                            yield new SeasonDiscount(id, description, isSpecialOffer, months, value);
                        }
                        default -> throw new DAOException("Unknown discount type: " + type);
                    };
                } else {
                    throw new DAOException("Discount with ID " + id + " not found.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error during SELECT from Discounts: " + e.getMessage(), e);
        }

        return discount;
    }

    public void updateDiscount(DiscountStrategy discount) throws DAOException {
        String sql = "UPDATE Discounts SET value = ?, userCategory = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, discount.getValue());

            // Aggiorna userCategory se è un CustomerBasedDiscount
            if (discount instanceof CustomerBasedDiscount customerDiscount) {
                statement.setString(2, customerDiscount.getCustomerCategory() != null ? customerDiscount.getCustomerCategory().name() : null);
            } else {
                statement.setNull(2, java.sql.Types.VARCHAR);
            }
            statement.setInt(3, discount.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Updating discount with ID " + discount.getId() + " failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DAOException("Error during UPDATE on Discounts: " + e.getMessage(), e);
        }

        // Gestisce l'aggiornamento dei mesi solo per gli sconti stagionali
        if (discount instanceof SeasonDiscount) {
            SeasonDiscount seasonalDiscount = (SeasonDiscount) discount;
            updateMonthsForSeasonalDiscount(discount.getId(), seasonalDiscount.getMonthsWithDiscount());
        }
    }

    private void updateMonthsForSeasonalDiscount(int discountId, HashSet<Month> months)  {
        deleteMonthsForDiscount(discountId);

        if (months != null && !months.isEmpty()) {
            addMonthsToDiscount(discountId, months);
        }
    }

    private void deleteMonthsForDiscount(int discountId) throws DAOException {
        String sql = "DELETE FROM MonthWithDiscounts WHERE discount_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, discountId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error during DELETE from MonthWithDiscounts: " + e.getMessage(), e);
        }
    }

}
