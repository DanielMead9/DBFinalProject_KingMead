import java.sql.*;
import java.util.ArrayList;

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

    public boolean displayProducts(String user, String password, ArrayList<String> output) {
        Connection myConnection = getMySQLConnection("DigitalInventory", user, password);
        if (myConnection == null) {
            System.out.println("Failed to obstain a valid connection. Stored procedure could not be run");
            return false;
        }
        try {
            CallableStatement myStoredProcedureCall = myConnection.prepareCall("{Call GetProducts()}");
            ResultSet rs = myStoredProcedureCall.executeQuery();

            while (rs.next()) {
                String myName = rs.getString("ProductName");
                int myAST = rs.getInt("AmountStorage");
                int myASH = rs.getInt("AmountShelf");
                System.out.println("ProductName:" + myName + ", Storage:" + myAST + ", Shelf" + myASH);
            }

        } catch (SQLException myException) {
            System.out.println("Failed to execute stored procedure:" + myException.getMessage());
            return false;
        }
        return true;
    }

}
