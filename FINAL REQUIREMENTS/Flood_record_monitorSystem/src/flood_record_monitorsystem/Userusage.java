/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flood_record_monitorsystem;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author User
 */
public class Userusage {
    private static int personnelId;
    private static String username;
    
    public static void setUser(int id, String user) {
        personnelId = id;
        username = user;
    }
    
    public static int getUserId() {
        return personnelId;
    }
    
    public static String getUsername() {
        return username;
    }
    
    public static void clearSession() {
        personnelId = 0;
        username = null;
        
    }
}
