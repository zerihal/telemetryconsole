package telemetryconsole.com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import telemetryconsole.com.example.Common.AccessLevel;
import telemetryconsole.com.example.Common.User;
import telemetryconsole.com.example.Common.UserDetails;

public class Authenticate {

    // The below may need to be made public and perhaps stated in the communication diagram
    // Maybe better in its own class?
    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:C://sqlite/db/consoleUsers.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public User AuthenticateUser(UserDetails userDetails) {

        // Suggested operations:
        // 1) Associate with userdetails (set to private variable)
        // 2) Create Connection object (look into basic security)
        // 3) Check user (pass connection object)
        // 4) Create new User with approriate access level
        // 5) Return the new instance of user

        return new User(userDetails); // Default return - might need to change!
    }
    
    public AccessLevel checkUser(UserDetails userDetails) {

        String sql = "SELECT username, password, accesslevel "
                    + "FROM users WHERE username = ?";
        
        try (Connection conn = connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)) {
            
            // set the value
            pstmt.setString(1, userDetails.getUsername());
            //
            ResultSet rs  = pstmt.executeQuery();

            if (rs != null) {
                if (rs.getString("password").equals(userDetails.getPassword())) {
                    return AccessLevel.values()[rs.getInt("accesslevel")];
                } else {
                    System.out.println("User was found but password incorrect");;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return AccessLevel.NONE;
    }
}
