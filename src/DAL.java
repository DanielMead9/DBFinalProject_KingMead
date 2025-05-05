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
                System.out.println("ProductName:" + myName + ", Storage:" + myAST + ", Shelf:" + myASH);
            }

        } catch (SQLException myException) {
            System.out.println("Failed to execute stored procedure:" + myException.getMessage());
            return false;
        }
        return true;
    }

    public boolean updateProduct(String user, String password, String name, int stAmount, int shAmount) {
        Connection myConnection = getMySQLConnection("DigitalInventory", user, password);
        if (myConnection == null) {
            System.out.println("Failed to obstain a valid connection. Stored procedure could not be run");
            return false;
        }
        try {
            CallableStatement myStoredProcedureCall = myConnection.prepareCall("{Call UpdateProduct(?,?,?)}");
            myStoredProcedureCall.setString(1, name);
            myStoredProcedureCall.setInt(2, stAmount);
            myStoredProcedureCall.setInt(3, shAmount);
            myStoredProcedureCall.executeQuery();

        } catch (SQLException myException) {
            System.out.println("Failed to execute stored procedure:" + myException.getMessage());
            return false;
        }
        return true;
    }

    public boolean checkout(String user, String password, String[] items, int[] nI, double type, double revenue,
            ArrayList<Double> temp, double total) {
        Connection myConnection = getMySQLConnection("DigitalInventory", user, password);
        if (myConnection == null) {
            System.out.println("Failed to obstain a valid connection. Stored procedure could not be run");
            return false;
        }
        try {
            for (int i = 0; i < items.length; i++) {
                CallableStatement myStoredProcedureCall = myConnection.prepareCall("{Call GetSingleProduct(?)}");
                myStoredProcedureCall.setString(1, items[i]);
                ResultSet rs = myStoredProcedureCall.executeQuery();

                while (rs.next()) {
                    type = type + rs.getInt("SellPrice") * nI[i];
                    revenue = revenue + rs.getInt("SellPrice") * nI[i] - rs.getInt("CostPrice") * nI[i];
                    total = total + rs.getInt("SellPrice") * nI[i];
                    updateProduct(user, password, items[i], 0, -nI[i]);
                }

            }

            temp.add(type);
            temp.add(revenue);
            temp.add(total);

        } catch (SQLException myException) {
            System.out.println("Failed to execute stored procedure:" + myException.getMessage());
            return false;
        }
        return true;
    }

}
