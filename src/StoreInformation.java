import java.sql.*;

class StoreInformation {
    private final String adminUsername = "admin";
    private final String adminPassword = "admin123";

    public boolean validateAdmin(String username, String password) {
        return username.equals(adminUsername) && password.equals(adminPassword);
    }

    public void addProduct(Product product) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement checkStmt = conn.prepareStatement(
                    "SELECT productId FROM Products WHERE productName = ?");
            checkStmt.setString(1, product.getProductName());
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                System.out.println("⚠️ Product '" + product.getProductName() + "' already exists in the store.");
                return;
            }

            PreparedStatement insertStmt = conn.prepareStatement(
                    "INSERT INTO Products (productName, buyingPrice, sellingPrice, availableQuantity, category) " +
                            "VALUES (?, ?, ?, ?, ?)");
            insertStmt.setString(1, product.getProductName());
            insertStmt.setDouble(2, product.getBuyingPrice());
            insertStmt.setDouble(3, product.getSellingPrice());
            insertStmt.setInt(4, product.getAvailableQuantity());
            insertStmt.setString(5, product.getCategory());

            insertStmt.executeUpdate();
            System.out.println("✅ Product '" + product.getProductName() + "' added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void listProducts() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Products")) {
            while (rs.next()) {
                System.out.println("Product ID: " + rs.getInt("productId") +
                        ", Name: " + rs.getString("productName") +
                        ", Buying Price: $" + rs.getDouble("buyingPrice") +
                        ", Selling Price: $" + rs.getDouble("sellingPrice") +
                        ", Quantity: " + rs.getInt("availableQuantity") +
                        ", Category: " + rs.getString("category"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void calculateProfitByCategory() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT category, SUM(sellingPrice - buyingPrice) AS totalProfit FROM Products GROUP BY category")) {
            System.out.println("📊 Profit by Category:");
            while (rs.next()) {
                System.out.println("Category: " + rs.getString("category") + " | Profit: $" + rs.getDouble("totalProfit"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // **Fix: Move Customer Methods Here**
    public boolean validateCustomer(String username, String password) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM Users WHERE username = ? AND password = ?")) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void registerCustomer(String username, String emailId, String password) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO Users (username, emailId, password, superCoins) VALUES (?, ?, ?, 100)")) {
            stmt.setString(1, username);
            stmt.setString(2, emailId);
            stmt.setString(3, password);
            stmt.executeUpdate();
            System.out.println("✅ Customer Registered Successfully!");
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                System.out.println("❌ Username already exists. Try a different username.");
            } else {
                e.printStackTrace();
            }
        }
    }

    public void purchaseProduct(String productName, int quantity, String username) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            // Retrieve userId from the Users table
            PreparedStatement userStmt = conn.prepareStatement(
                    "SELECT userId FROM Users WHERE username = ?");
            userStmt.setString(1, username);
            ResultSet userRs = userStmt.executeQuery();

            if (!userRs.next()) {
                System.out.println("❌ User not found.");
                return;
            }
            int userId = userRs.getInt("userId");

            // Check if the product exists
            PreparedStatement checkStmt = conn.prepareStatement(
                    "SELECT productId, availableQuantity, sellingPrice FROM Products WHERE productName = ?");
            checkStmt.setString(1, productName);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("❌ Product not found.");
                return;
            }

            int productId = rs.getInt("productId");
            int availableStock = rs.getInt("availableQuantity");
            double price = rs.getDouble("sellingPrice");
            double totalCost = price * quantity;

            if (availableStock < quantity) {
                System.out.println("❌ Not enough stock. Available: " + availableStock);
                return;
            }

            // Deduct stock
            PreparedStatement updateStmt = conn.prepareStatement(
                    "UPDATE Products SET availableQuantity = availableQuantity - ? WHERE productId = ?");
            updateStmt.setInt(1, quantity);
            updateStmt.setInt(2, productId);
            int updatedRows = updateStmt.executeUpdate();

            if (updatedRows == 0) {
                System.out.println("❌ Stock update failed.");
                return;
            }

            // Insert into Orders table
            PreparedStatement orderStmt = conn.prepareStatement(
                    "INSERT INTO Orders (userId, productId, quantity, totalCost, orderDate) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)");
            orderStmt.setInt(1, userId);
            orderStmt.setInt(2, productId);
            orderStmt.setInt(3, quantity);
            orderStmt.setDouble(4, totalCost);
            int insertedRows = orderStmt.executeUpdate();

            if (insertedRows == 0) {
                System.out.println("❌ Order insertion failed.");
                return;
            }

            conn.commit();
            System.out.println("✅ Purchase successful! Order added.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}