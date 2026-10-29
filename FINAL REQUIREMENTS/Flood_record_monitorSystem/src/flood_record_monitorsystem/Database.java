/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flood_record_monitorsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author User
 */
public class Database {
    
    public static Connection connect(){
    try{
           String url = "jdbc:MySQL://localhost:3306/flood_system";
           String user = "root";
           String pass = "";
           
           Connection conn = DriverManager.getConnection(url, user, pass);
           return conn;
}   catch (SQLException e) {
    System.out.println("Database connection failed: " + e.getMessage());
    return null;
}
}
    
}