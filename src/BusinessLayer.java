import java.util.ArrayList;

public class BusinessLayer {
    DAL dal = new DAL();
    private String userName = "";
    private String password = "";
    private boolean success = false;
    private ArrayList<String> output = new ArrayList<>();
    private double card = 0;
    private double cash = 0;
    private double profit = 0;
    private double total = 0;
    private double amountSpent = 0;
    private boolean calculated = false;

    public boolean checkConnection(String un, String pw) {
        this.userName = un;
        this.password = pw;
        success = dal.ensureConnection("DigitalInventory", userName, password);
        return success;
    }

    public void displayProducts() {
        output = new ArrayList<>();
        dal.displayProducts(userName, password, output);
        printData(output);
    }

    public void updateProduct(String name, int stAmount, int shAmount) {
        dal.updateProduct(userName, password, name, stAmount, shAmount);
    }

    public void displayMoney() {
        getSpent();
        double revenue = (card + cash);
        System.out.println("----------------------");
        System.out.printf("Total cost: $%.2f%n", amountSpent);
        System.out.printf("Total Revenue: $%.2f%n", revenue);
        System.out.printf("Paid for with Card: $%.2f%n", card);
        System.out.printf("Paid for with Cash: $%.2f%n", cash);
        System.out.println("----------------------");
        System.out.printf("Total Profit on all products: $%.2f%n", (revenue - amountSpent));
        System.out.printf("Total Profit on sold products: $%.2f%n", profit);

    }

    public void getSpent() {
        if (!calculated) {
            amountSpent = dal.getSpent(userName, password);
            calculated = true;
        }
    }

    public void checkout(String[] items, int[] nI, String type) {
        total = 0;
        ArrayList<Double> temp = new ArrayList<>();
        if (type.equalsIgnoreCase("cash")) {
            dal.checkout(userName, password, items, nI, cash, profit, temp, total);
            cash += temp.get(0);
        } else {
            dal.checkout(userName, password, items, nI, card, profit, temp, total);
            card += temp.get(0);
        }
        profit += temp.get(1);
        total = temp.get(2);

    }

    public void largeOrder(String buyerName, String location, String phone, String email, String[] items,
            int[] quantities) {
        if (items == null || quantities == null || items.length != quantities.length || items.length == 0) {
            System.out.println("Invalid order data.");
            return;
        }

        // Process the large order
        dal.processLargeOrder(userName, password, buyerName, location, phone, email, items, quantities);

        System.out.println("Order has been successfully processed for: " + buyerName);
    }

    public void printData(ArrayList<String> output) {
        for (int i = 0; i < output.size(); i++) {
            System.out.println(output.get(i));
        }
    }
}