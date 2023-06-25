package telemetryconsole.com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import telemetryconsole.com.example.Common.AccessLevel;
import telemetryconsole.com.example.Common.User;
import telemetryconsole.com.example.Common.UserDBConnector;
import telemetryconsole.com.example.Common.UserDbException;
import telemetryconsole.com.example.Common.UserDetails;

public class Authenticate {

    private UserDetails _userDetails;
    private UserDBConnector _userDbConnector;

    public UserDetails get_userDetails() {
        return _userDetails;
    }

    public void set_userDetails(UserDetails _userDetails) {
        this._userDetails = _userDetails;
    }

    public Authenticate(UserDetails userDetails) {
        
        // 1) Associate userDetails with self
        set_userDetails(userDetails);

        // 2) Create new private instance of DB connector
        _userDbConnector = new UserDBConnector();
    }

    public User AuthenticateUser() {
       
        User user;

        // 3) Check user
        AccessLevel accessLevel;
        try {
            accessLevel = checkUser(get_userDetails());
            user = new User(_userDetails, accessLevel);
        } catch (UserDbException e) {
            // Create an instance of "error user" to return so that the caller knows
            // that there has been an exception and can take some appropriate action
            // if required
            user = new User(true);
        }

        // 5) Return the new instance of user
        return user;
    }
    
    public AccessLevel checkUser(UserDetails userDetails) throws UserDbException {

        String sql = "SELECT username, password, accesslevel "
                    + "FROM users WHERE username = ?";
        
        try (Connection conn = _userDbConnector.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)) {
            
            // set the value
            pstmt.setString(1, userDetails.getUsername());
            //
            ResultSet rs  = pstmt.executeQuery();

            if (rs != null) {
                if (rs.getString("password").equals(userDetails.getPassword())) {
                    return AccessLevel.values()[rs.getInt("accesslevel")];
                } else {
                    System.out.println("User was found but password incorrect");
                    return AccessLevel.NONE;
                }
            } else {
                // Invalid user (not found)
                return AccessLevel.INVALID;
            }
        } catch (SQLException e) {
            // Exception - re-throw with added userDetails
            throw new UserDbException(userDetails, e);
        }
    }
}
