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

    public UserDBConnector get_userDbConnector() {
        return _userDbConnector;
    }

    public Authenticate(String username, String password) {
        
        // Create and set a new instance of UserDetails. This has a public setter so if user has entered something
        // incorrectly then this instance of Authenticate can be reused by just updating UserDetails and rerunning 
        // DoAuthentication.
        set_userDetails(new UserDetails(username, password));

        // Create new instance of DB connector. This is really just for this instance of Authenticate, but a getter 
        // is included for testing purposes. This is required to check the user from the authetication operation
        _userDbConnector = new UserDBConnector();
    }

    public User DoAuthentication() {
       
        User user;

        // Do authentication by calling CheckUser to check the users details (username and password) obtain the 
        // access level. This will be USER or ADMIN if user authenticated, NONE if password incorrect or INVALID
        // if the user (username) does not exist   
        AccessLevel accessLevel;
        try {
            accessLevel = CheckUser(get_userDetails());
            user = new User(_userDetails, accessLevel);
        } catch (UserDbException e) {
            // Create an instance of "error user" to return so that the caller knows
            // that there has been an exception and can take some appropriate action
            // if required
            user = new User(true);
        }

        // Return the new instance of user (this can then be linked to the caller)
        return user;
    }
    
    public AccessLevel CheckUser(UserDetails userDetails) throws UserDbException {

        // Set initial query string for the user DB query
        String sql = "SELECT username, password, accesslevel "
                    + "FROM users WHERE username = ?";
        
        try (Connection conn = get_userDbConnector().connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Set the value in prepared statement for username to that supplied in UserDetails
            pstmt.setString(1, userDetails.getUsername());
            
            // Execute the DB query
            ResultSet rs = pstmt.executeQuery();
            
            if (rs != null && rs.next() != false) {

                if (rs.getString("password").equals(userDetails.getPassword())) {
                    // Password matched - get access level from the query result
                    return AccessLevel.values()[rs.getInt("accesslevel")];
                } else {
                    // Result was returned for this user, however password did not match (so was
                    // incorrect) - return access level of none to indicate this (which can be
                    // futher handled by the caller as appropriate)
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
