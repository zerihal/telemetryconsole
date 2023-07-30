package telemetryconsole.com.example.Common;

public class User {
    private UserDetails userDetails;
    private AccessLevel accessLevel;

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public User(UserDetails userdetails, AccessLevel accessLevel) {    
        this.userDetails = userdetails;
        this.accessLevel = accessLevel;
    }

    public User(Boolean errorUser) {
        if (errorUser) {
            this.userDetails = new UserDetails(DefaultStrings.ErrorUser, null);
            this.accessLevel = AccessLevel.INVALID;
        }
    }
}
