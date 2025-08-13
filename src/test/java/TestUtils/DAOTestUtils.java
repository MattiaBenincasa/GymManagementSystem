package TestUtils;

import ORM.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DAOTestUtils {

    public static void resetDatabase() {
        Connection connection = ConnectionManager.getSingleInstance().getConnection();

        String[] tables = {
                "appointment",
                "booking",
                "registrationfee",
                "customermedcertificate",
                "customermembership",
                "coursemembership",
                "coursetrainer",
                "customerfee",
                "wrmembership",
                "membershipinbundle",
                "bundle",
                "bundlediscounts",
                "discounts",
                "membershipdiscounts",
                "monthwithdiscounts",
                "membership",
                "dailyclass",
                "course",
                "traineravailability",
                "trainer",
                "customers",
                "User"
        };

        String[] sequences = {
                "User_id_seq",
                "traineravailability_id_seq",
                "course_id_seq",
                "dailyclass_id_seq",
                "membership_id_seq",
                "bundle_id_seq",
                "registrationfee_id_seq",
                "customerfee_id_seq",
                "discounts_id_seq"
        };

        try (Statement statement = connection.createStatement()) {

            for (String table : tables) {
                String resetRows = "DELETE FROM \"" + table + "\"";
                statement.executeUpdate(resetRows);
            }

            for (String sequence : sequences) {
                String resetSequence = "ALTER SEQUENCE \"" + sequence + "\" RESTART WITH 1";
                statement.executeUpdate(resetSequence);
            }

        } catch (SQLException e) {
            System.err.println("ERROR DURING DB RESET: " + e.getMessage());
        }
    }

}
