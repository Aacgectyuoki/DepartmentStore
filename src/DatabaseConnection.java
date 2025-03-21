import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:OnlineStore.db"; // SQLite database file

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // Method to create tables if they don't exist
    public static void initializeDatabase() {
        String createProductsTable = "CREATE TABLE IF NOT EXISTS Products (" +
                "productId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "productName TEXT NOT NULL UNIQUE, " +
                "buyingPrice REAL NOT NULL, " +
                "sellingPrice REAL NOT NULL, " +
                "availableQuantity INTEGER NOT NULL, " +
                "category TEXT NOT NULL)";

        String createUsersTable = "CREATE TABLE IF NOT EXISTS Users (" +
                "userId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL UNIQUE, " +
                "emailId TEXT NOT NULL, " +
                "password TEXT NOT NULL, " +
                "superCoins INTEGER DEFAULT 100)";

        String createOrdersTable = "CREATE TABLE IF NOT EXISTS Orders (" +
                "orderId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userId INTEGER NOT NULL, " +
                "productId INTEGER NOT NULL, " +
                "quantity INTEGER NOT NULL, " +
                "totalCost REAL NOT NULL, " +
                "orderDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY(userId) REFERENCES Users(userId), " +
                "FOREIGN KEY(productId) REFERENCES Products(productId))";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createProductsTable);
            stmt.execute(createUsersTable);
            stmt.execute(createOrdersTable);
            System.out.println("✅ Database initialized successfully!");
        } catch (SQLException e) {
            System.err.println("❌ Database initialization failed: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        initializeDatabase();
    }
}
