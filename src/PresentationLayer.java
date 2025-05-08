
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

        while (choice != -1) {
            System.out.println(
                    "\nOptions \n1.Display Products\n2.Checkout\n3.Display Profit\n4.Create Large Order\n5. \n6.Update Product amount");

            System.out.println("-1.Quit \nWhat would you like to do? (input a number): ");
            choice = sc.nextInt();
            System.out.println();

            switch (choice) {
                case 1:
                    bl.displayProducts();
                    break;

                case 2:
                    System.out.println("How many different items are being bought?");
                    int num = sc.nextInt();

                    String[] items = new String[num];
                    int[] numItems = new int[num];
                    for (int i = 0; i < num; i++) {
                        sc.nextLine();
                        System.out.println("Item name: ");
                        items[i] = sc.nextLine();
                        System.out.println("number of that item: ");
                        numItems[i] = sc.nextInt();
                    }
                    System.out.println("Payment Type (Cash or Card): ");
                    String type = sc.next();
                    bl.checkout(items, numItems, type);
                    System.out.println();
                    break;

                case 3:
                    bl.displayMoney();
                    break;

                case 4:

                    break;

                case 5:

                    break;

                case 6:
                    sc.nextLine();
                    System.out.println("What product would you like to update?: ");
                    String name = sc.nextLine();
                    System.out.println("How many did you add to storage? (if removed put a negative amount)");
                    int stAmount = sc.nextInt();
                    System.out.println("How many did you add to Shelf? (if removed put a negative amount)");
                    int shAmount = sc.nextInt();

                    bl.updateProduct(name, stAmount, shAmount);
                    break;

                case 7:

                    break;
            }

        }
        System.out.println("Goodbye. Have a nice Day");
    }

}
