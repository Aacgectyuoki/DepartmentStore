import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class User {
    private String username;
    private String emailId;
    private String password;
    private int superCoins;

    public User(String username, String emailId, String password) {
        this.username = username;
        this.emailId = emailId;
        this.password = password;
        this.superCoins = 100; // Welcome bonus
    }

    public String getUsername() { return username; }
    public String getEmailId() { return emailId; }
    public String getPassword() { return password; }
    public int getSuperCoins() { return superCoins; }

    public boolean isUsernameTaken(String username) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Users WHERE username = ?")) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // If any result exists, username is taken
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void registerCustomer(User user) {
        if (isUsernameTaken(user.getUsername())) {
            System.out.println("Username already exists. Try a different username.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO Users (username, emailId, password) VALUES (?, ?, ?)")) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmailId());
            stmt.setString(3, user.getPassword());
            stmt.executeUpdate();
            System.out.println("Customer Registered Successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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

    public void deductSuperCoins(String username, int coins) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE Users SET superCoins = superCoins - ? WHERE username = ? AND superCoins >= ?")) {
            stmt.setInt(1, coins);
            stmt.setString(2, username);
            stmt.setInt(3, coins);
            int updatedRows = stmt.executeUpdate();
            if (updatedRows > 0) {
                System.out.println("✅ SuperCoins deducted successfully.");
            } else {
                System.out.println("❌ Not enough SuperCoins.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "User: " + username + ", Email: " + emailId + ", SuperCoins: " + superCoins;
    }
}