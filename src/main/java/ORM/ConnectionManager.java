package ORM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;

public class ConnectionManager {
    private Connection connection;
    static private ConnectionManager singleInstance;

    private ConnectionManager() {
        Dotenv dotenv = Dotenv.load();
        String url = dotenv.get("DB_URL");
        String userName = dotenv.get("DB_USR");
        String password = dotenv.get("DB_PASS");
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(url, userName, password);
            System.out.println("Connection ok! ");
        }catch (ClassNotFoundException e) {
            System.err.println("Postgresql Driver not found");
        }catch (SQLException e) {
            System.err.println("Error during db connection: " + e.getMessage());
        }
    }

    public static ConnectionManager getSingleInstance() {
        if (singleInstance == null)
            singleInstance = new ConnectionManager();

        return singleInstance;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void closeConnection() {
        try{
            this.connection.close();
            this.connection = null;
        } catch (Exception e){
            System.out.println("Error while closing database connection");
        }
    }

}
