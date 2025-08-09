package ORM.Membership;

import BusinessLogic.Exceptions.DAOException;
import DomainModel.DiscountStrategy.DiscountStrategy;
import DomainModel.Membership.Bundle;
import DomainModel.Membership.Membership;
import ORM.ConnectionManager;
import ORM.DiscountStrategy.DiscountsDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BundleDAO {
    private final Connection connection;
    private final DiscountsDAO discountsDAO;
    private final MembershipDAO membershipDAO;

    public BundleDAO(DiscountsDAO discountsDAO, MembershipDAO membershipDAO) {
        this.connection = ConnectionManager.getSingleInstance().getConnection();
        this.discountsDAO = discountsDAO;
        this.membershipDAO = membershipDAO;
    }

    public Bundle createBundle(Bundle bundle) {
        String sql = "INSERT INTO Bundle (name, description) VALUES (?, ?)";
        int bundleId;

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, bundle.getName());
            statement.setString(2, bundle.getDescription());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Creating bundle failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    bundleId = generatedKeys.getInt(1);
                    Bundle bundleWithID =  new Bundle(bundleId);
                    bundleWithID.setName(bundle.getName());
                    bundleWithID.setDescription(bundle.getDescription());
                    for (DiscountStrategy discountStrategy : bundle.getDiscounts())
                        bundleWithID.addDiscount(discountStrategy);

                    for (Membership membership : bundle.getMemberships())
                        bundleWithID.addMembership(membership);

                    addMembershipsToBundle(bundleId, bundle.getMemberships());
                    addDiscountsToBundle(bundleId, bundle.getDiscounts());

                    return bundleWithID;
                } else {
                    throw new DAOException("Creating bundle failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error during INSERT into Bundle: " + e.getMessage(), e);
        }
    }

    public void updateBundle(Bundle bundle) {
        String sql = "UPDATE Bundle SET name = ?, description = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, bundle.getName());
            statement.setString(2, bundle.getDescription());
            statement.setInt(3, bundle.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Updating bundle with ID " + bundle.getId() + " failed, no rows affected.");
            }

            updateMembershipsForBundle(bundle.getId(), bundle.getMemberships());
            updateDiscountsForBundle(bundle.getId(), bundle.getDiscounts());
        } catch (SQLException e) {
            throw new DAOException("Error during UPDATE on Bundle: " + e.getMessage(), e);
        }
    }

    public void deleteBundle(int bundleId) throws DAOException {
        String sql = "DELETE FROM Bundle WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bundleId);
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new DAOException("Deletion of bundle with ID " + bundleId + " failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DAOException("Error during DELETE from Bundle: " + e.getMessage(), e);
        }
    }

    public Bundle getBundleByID(int bundleId) {
        String sql = "SELECT id, name, description FROM Bundle WHERE id = ?";
        Bundle bundle = null;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bundleId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");

                    bundle = new Bundle(id);
                    bundle.setName(name);
                    bundle.setDescription(description);
                    List<Membership> memberships = getMembershipsForBundle(id);
                    List<DiscountStrategy> discounts = getDiscountsForBundle(id);

                    for (Membership membership : memberships)
                        bundle.addMembership(membership);

                    for (DiscountStrategy discountStrategy : discounts)
                        bundle.addDiscount(discountStrategy);

                    return bundle;
                } else {
                    throw new DAOException("Bundle with ID " + bundleId + " not found.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error during SELECT from Bundle: " + e.getMessage(), e);
        }
    }

    // ------------------- Helper methods -------------------

    private void updateDiscountsForBundle(int bundleId, List<DiscountStrategy> discounts) throws DAOException {
        deleteDiscountsForBundle(bundleId);
        addDiscountsToBundle(bundleId, discounts);
    }

    private void updateMembershipsForBundle(int bundleId, List<Membership> memberships) throws DAOException {
        deleteMembershipsForBundle(bundleId);
        addMembershipsToBundle(bundleId, memberships);
    }

    private void deleteDiscountsForBundle(int bundleId) throws DAOException {
        String sql = "DELETE FROM BundleDiscounts WHERE bundle_id = ?";
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setInt(1, bundleId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error during DELETE from BundleDiscounts: " + e.getMessage(), e);
        }
    }

    private void deleteMembershipsForBundle(int bundleId) throws DAOException {
        String sql = "DELETE FROM MembershipInBundle WHERE bundle_id = ?";
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setInt(1, bundleId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error during DELETE from MembershipInBundle: " + e.getMessage(), e);
        }
    }

    private void addDiscountsToBundle(int bundleId, List<DiscountStrategy> discounts) {
        if (discounts == null || discounts.isEmpty()) return;

        String sql = "INSERT INTO BundleDiscounts (bundle_id, discount_id) VALUES (?, ?)";
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            for (DiscountStrategy discount : discounts) {
                statement.setInt(1, bundleId);
                statement.setInt(2, discount.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new DAOException("Error during batch INSERT into BundleDiscounts: " + e.getMessage(), e);
        }
    }

    private void addMembershipsToBundle(int bundleId, List<Membership> memberships) {
        if (memberships == null || memberships.isEmpty()) return;

        String sql = "INSERT INTO MembershipInBundle (bundle_id, membership_id) VALUES (?, ?)";
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            for (Membership membership : memberships) {
                statement.setInt(1, bundleId);
                statement.setInt(2, membership.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new DAOException("Error during batch INSERT into MembershipInBundle: " + e.getMessage(), e);
        }
    }

    private List<DiscountStrategy> getDiscountsForBundle(int bundleId) {
        List<DiscountStrategy> discounts = new ArrayList<>();
        String sql = "SELECT discount_id FROM BundleDiscounts WHERE bundle_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bundleId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int discountId = resultSet.getInt("discount_id");
                    DiscountStrategy discount = this.discountsDAO.getDiscountByID(discountId);
                    discounts.add(discount);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error getting discounts for bundle: " + e.getMessage(), e);
        }
        return discounts;
    }

    private List<Membership> getMembershipsForBundle(int bundleId) throws DAOException {
        List<Membership> memberships = new ArrayList<>();
        String sql = "SELECT membership_id FROM MembershipInBundle WHERE bundle_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bundleId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int membershipId = resultSet.getInt("membership_id");
                    Membership membership = membershipDAO.getMembershipByID(membershipId);
                    memberships.add(membership);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error getting memberships for bundle: " + e.getMessage(), e);
        }
        return memberships;
    }
}
