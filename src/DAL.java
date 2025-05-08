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
                System.out.println("Product Name: " + myName + ", Storage: " + myAST + ", Shelf: " + myASH);
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
            System.out.println("----------------------------------");
            for (int i = 0; i < items.length; i++) {
                CallableStatement myStoredProcedureCall = myConnection.prepareCall("{Call GetSingleProduct(?)}");
                myStoredProcedureCall.setString(1, items[i]);
                ResultSet rs = myStoredProcedureCall.executeQuery();

                while (rs.next()) {
                    type = type + rs.getDouble("SellPrice") * nI[i];
                    revenue = revenue + rs.getDouble("SellPrice") * nI[i] - rs.getDouble("CostPrice") * nI[i];
                    total = total + rs.getDouble("SellPrice") * nI[i];
                    updateProduct(user, password, items[i], 0, -nI[i]);
                    System.out.printf("%s $%4.2f x%2d %n", items[i], rs.getDouble("SellPrice"), nI[i]);
                }

            }
            System.out.println("----------------------------------");
            System.out.printf("Total: $%.2f", total);

            temp.add(type);
            temp.add(revenue);
            temp.add(total);

        } catch (SQLException myException) {
            System.out.println("Failed to execute stored procedure:" + myException.getMessage());
            return false;
        }
        return true;
    }

    public boolean processLargeOrder(String user, String password, String buyerName, String location, String phone,
            String email, String[] items, int[] quantities) {
        Connection myConnection = getMySQLConnection("DigitalInventory", user, password);
        if (myConnection == null) {
            System.out.println("Failed to obtain a valid connection. Stored procedure could not be run.");
            return false;
        }

        try {
            for (int i = 0; i < items.length; i++) {
                CallableStatement updateCall = myConnection.prepareCall("{Call UpdateStorageOnly(?, ?)}");
                updateCall.setString(1, items[i]);
                updateCall.setInt(2, -quantities[i]);
                updateCall.execute();
            }

            CallableStatement logCall = myConnection.prepareCall("{Call LogLargeOrder(?, ?, ?, ?)}");
            logCall.setString(1, buyerName);
            logCall.setString(2, location);
            logCall.setString(3, phone);
            logCall.setString(4, email);
            logCall.execute();

            System.out.println("Large order processed and logged successfully.");
            return true;

        } catch (SQLException myException) {
            System.out.println("Failed to execute large order procedure: " + myException.getMessage());
            return false;
        }
    }


    public double getSpent(String user, String password) {
        Connection myConnection = getMySQLConnection("DigitalInventory", user, password);
        if (myConnection == null) {
            System.out.println("Failed to obstain a valid connection. Stored procedure could not be run");
        }
        try {
            double spent = 0;
            CallableStatement myStoredProcedureCall = myConnection.prepareCall("{Call GetProducts()}");
            ResultSet rs = myStoredProcedureCall.executeQuery();

            while (rs.next()) {
                double myCost = rs.getDouble("CostPrice");
                int myAST = rs.getInt("AmountStorage");
                int myASH = rs.getInt("AmountShelf");
                spent += myCost * (myASH + myAST);
            }

            return spent;

        } catch (SQLException myException) {
            System.out.println("Failed to execute stored procedure:" + myException.getMessage());
            return -1;
        }


    public boolean restockProduct(String user, String password, String name, int quantity) {
        Connection myConnection = getMySQLConnection("DigitalInventory", user, password);
        if (myConnection == null) {
            System.out.println("Failed to get connection. Restock aborted.");
            return false;
        }
    
        try {
            // First, check if there's enough in storage
            CallableStatement checkCall = myConnection.prepareCall("{CALL GetSingleProduct(?)}");
            checkCall.setString(1, name);
            ResultSet rs = checkCall.executeQuery();
    
            if (rs.next()) {
                int availableStorage = rs.getInt("AmountStorage");
                if (availableStorage < quantity) {
                    System.out.println("Not enough in storage to restock.");
                    return false;
                }
    
                // Now move from storage to shelf using existing UpdateProduct
                return updateProduct(user, password, name, -quantity, quantity);
            }
        } catch (SQLException e) {
            System.out.println("Restock failed: " + e.getMessage());
            return false;
        }
        return false;

    }

}
