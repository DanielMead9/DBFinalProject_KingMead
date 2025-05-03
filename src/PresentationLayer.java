
import java.util.Scanner;

public class PresentationLayer {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        boolean connected = false;
        int choice = 0;
        BusinessLayer bl = new BusinessLayer();
        String userName = "";
        String password = "";

        while (!connected) {
            System.out.println("Enter username and password:");

            userName = sc.nextLine();
            if (userName.equals("-1")) {
                choice = -1;
                return;
            }
            password = sc.nextLine();

            if (bl.checkConnection(userName, password)) {
                System.out.println("Successfully connected to the database \n");
                connected = true;
            } else {
                System.out.println(
                        "Failed to connect to the database. Renter Username and Password and ensure you have the DigitalInventory Database. Enter -1 to close");
            }
        }
    }

}
