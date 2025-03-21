import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

class StoreInformation {
    private Map<Integer, Product> products = new ConcurrentHashMap<>();
    private List<User> users = Collections.synchronizedList(new ArrayList<>());
    private final String adminUsername = "admin";
    private final String adminPassword = "admin123";

    public void addProduct(Product product) {
        products.put(product.getProductId(), product);
    }

    public boolean validateAdmin(String username, String password) {
        return username.equals(adminUsername) && password.equals(adminPassword);
    }

    public void listProducts() {
        products.values().forEach(System.out::println);
    }

    public Product searchProductByName(String productName) {
        return products.values().stream()
                .filter(product -> product.getProductName().equalsIgnoreCase(productName))
                .findFirst()
                .orElse(null);
    }

    public void purchaseProduct(String productName, int quantity) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                         "UPDATE Products SET availableQuantity = availableQuantity - ? WHERE productName = ? AND availableQuantity >= ?")) {

                stmt.setInt(1, quantity);
                stmt.setString(2, productName);
                stmt.setInt(3, quantity);

                int updatedRows = stmt.executeUpdate();
                if (updatedRows > 0) {
                    System.out.println("Purchase successful!");
                    break; // Exit loop after successful purchase
                } else {
                    System.out.println("Product not found or out of stock. Enter another product name: ");
                    productName = scanner.nextLine();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                break;
            }
        }
    }


    public User registerCustomer(String username, String emailId, String password) {
        User newUser = new User(username, emailId, password);
        users.add(newUser);
        return newUser;
    }

    public boolean validateCustomer(String username, String password) {
        return users.stream().anyMatch(user -> user.getUsername().equals(username) && user.getPassword().equals(password));
    }
}