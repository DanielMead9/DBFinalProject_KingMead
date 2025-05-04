import java.util.ArrayList;

public class BusinessLayer {
    DAL dal = new DAL();
    private String userName = "";
    private String password = "";
    private boolean success = false;
    private ArrayList<String> output = new ArrayList<>();

    public boolean checkConnection(String un, String pw) {
        this.userName = un;
        this.password = pw;
        success = dal.ensureConnection("DigitalInventory", userName, password);
        System.out.println(userName);
        System.out.println(password);
        return success;
    }

    public void displayProducts() {
        output.clear();
        dal.displayProducts(userName, password, output);
        printData(output);
    }

    public void printData(ArrayList<String> output) {
        for (int i = 0; i < output.size(); i++) {
            System.out.println(output.get(i));
        }
    }
}