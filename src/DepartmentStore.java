import java.util.Scanner;

public class DepartmentStore {
    public static void main(String[] args) {
        StoreInformation store = new StoreInformation();
        store.addProduct(new Product(1, "Rice", 30.0, 50, "Grocery"));
        store.addProduct(new Product(2, "Notebook", 5.0, 100, "Stationary"));
        store.addProduct(new Product(3, "Shampoo", 15.0, 40, "Toiletry"));
        store.addProduct(new Product(4, "Carrots", 10.0, 60, "Vegetables"));

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("1. Admin Login");
            System.out.println("2. Customer Login");
            System.out.println("3. Register Customer");
            System.out.println("4. Purchase Product");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 2: // Customer Login
                    System.out.print("Enter Username: ");
                    String loginUser = scanner.nextLine();
                    System.out.print("Enter Password: ");
                    String loginPass = scanner.nextLine();

                    if (store.validateCustomer(loginUser, loginPass)) {
                        System.out.println("Login successful! You can now purchase products.");

                        // Purchase flow after login
                        System.out.print("Enter Product Name: ");
                        String productName = scanner.nextLine();
                        System.out.print("Enter Quantity: ");
                        int quantity = scanner.nextInt();
                        store.purchaseProduct(productName, quantity);
                    } else {
                        System.out.println("Invalid credentials. Try again.");
                    }
                    break;

                case 3:
                    System.out.print("Enter New Username: ");
                    String newUser = scanner.nextLine();
                    System.out.print("Enter Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter Password: ");
                    String newPass = scanner.nextLine();
                    store.registerCustomer(newUser, email, newPass);
                    System.out.println("Customer Registered Successfully!");
                    break;
                case 4:
                    System.out.print("Enter Product Name: ");
                    String productName = scanner.nextLine();
                    System.out.print("Enter Quantity: ");
                    int quantity = scanner.nextInt();
                    store.purchaseProduct(productName, quantity);
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid Choice. Try Again.");
            }
        }
        scanner.close();
    }
}