package thobani.memela.locationsharingapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import thobani.memela.locationsharingapp.models.User;

public class Database {
    public static final String URL = "jdbc:sqlite:locationsharing.db";

    // Initializes the database and creates tables if they don't exist
    public static void init() {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
             
            // Create Users table
            String sql = "CREATE TABLE IF NOT EXISTS Users (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "username TEXT UNIQUE," +
                         "password TEXT," +
                         "latitude REAL," +
                         "longitude REAL);";
            stmt.execute(sql);
            
            // Create Contacts table
            sql = "CREATE TABLE IF NOT EXISTS Contacts (" +
                  "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                  "userId INTEGER," +
                  "contactInfo TEXT," +
                  "contactType TEXT," +
                  "FOREIGN KEY (userId) REFERENCES Users(id));";
            stmt.execute(sql);
            
        } catch (SQLException e) {
            System.out.println("Database initialization error: " + e.getMessage());
        }
    }

    // Saves a new user to the database
    public static void saveUser(User user) {
        String sql = "INSERT INTO Users(username, password, latitude, longitude) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword()); // You should hash the password in a real app
            pstmt.setDouble(3, user.getLatitude());
            pstmt.setDouble(4, user.getLongitude());
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println("Error saving user: " + e.getMessage());
        }
    }

    // Retrieves a user by username from the database
    public static User getUserByUsername(String username) {
        String sql = "SELECT * FROM Users WHERE username = ?";
        User user = null;
        
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password")); // Password should be hashed in production
                user.setLatitude(rs.getDouble("latitude"));
                user.setLongitude(rs.getDouble("longitude"));
            }
            
        } catch (SQLException e) {
            System.out.println("Error retrieving user: " + e.getMessage());
        }
        
        return user;
    }
}
