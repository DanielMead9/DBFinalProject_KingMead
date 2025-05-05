import java.util.ArrayList;

public class BusinessLayer {
    DAL dal = new DAL();
    private String userName = "";
    private String password = "";
    private boolean success = false;
    private ArrayList<String> output = new ArrayList<>();
    private double card = 0;
    private double cash = 0;
    private double revenue = 0;
    private double total = 0;

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

    public void checkout(String[] items, int[] nI, String type) {
        total = 0;
        ArrayList<Double> temp = new ArrayList<>();
        if (type.equalsIgnoreCase("cash")) {
            dal.checkout(userName, password, items, nI, cash, revenue, temp, total);
            cash += temp.get(0);
        } else {
            dal.checkout(userName, password, items, nI, card, revenue, temp, total);
            card += temp.get(0);
        }
        revenue += temp.get(1);
        total = temp.get(2);
        System.out.println(total);
    }

    public void printData(ArrayList<String> output) {
        for (int i = 0; i < output.size(); i++) {
            System.out.println(output.get(i));
        }
    }
}