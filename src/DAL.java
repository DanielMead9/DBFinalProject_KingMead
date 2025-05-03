import java.sql.*;

public class DAL {
    private Connection getMySQLConnection(String databaseName, String user, String password) {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/" + databaseName, user, password);
        } catch (SQLException exception) {
            System.out.println("Failed to connect to the database" + exception.getMessage());
            return null;
        }
    }

    public boolean ensureConnection(String databaseName, String user, String password) {

        Connection myConnection = getMySQLConnection(databaseName, user, password);
        if (myConnection == null) {
            System.out.println("Failed to get a connection, cannot execute query");
            return false;
        }
        return true;
    }

}
