package flood_record_monitorsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class ActivityLogger {
    
    public static void logAction(String username, String role, String actionDescription) {
        String url = "jdbc:mysql://localhost:3306/flood_system";
        String user = "root";
        String pass = "";
        String query = "INSERT INTO tbl_activity_logs (username, role, action) VALUES (?, ?, ?)";
        
        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement pst = con.prepareStatement(query)) {
            
            // Validate parameters before logging
            if (username == null || username.trim().isEmpty()) {
                System.out.println("Warning: Activity log skipped due to null username.");
                return;
            }
            if (role == null || role.trim().isEmpty()) {
                System.out.println("Warning: Activity log skipped due to null role.");
                return;
            }
            if (actionDescription == null || actionDescription.trim().isEmpty()) {
                System.out.println("Warning: Activity log skipped due to null action description.");
                return;
            }
            
            pst.setString(1, username);
            pst.setString(2, role);
            pst.setString(3, actionDescription);
            
            pst.executeUpdate();
            System.out.println("Log Saved: " + actionDescription); // Debug output
            
        } catch (Exception e) {
            System.out.println("Error saving activity log to database: " + e.getMessage());
            e.printStackTrace(); // Optional but helpful for development
        }
    }
}