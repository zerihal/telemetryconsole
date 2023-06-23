package telemetryconsole.com.example.Common;

public class User {
    private UserDetails userDetails;
    private AccessLevel accessLevel;

    public UserDetails getUserDetails() {
        return userDetails;
    }
    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }
    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    public User(UserDetails userdetails, AccessLevel accessLevel) {    
        this(userdetails);
        setAccessLevel(accessLevel);
    }

    public User(UserDetails userDetails) {
        setUserDetails(userDetails);
    }
}
