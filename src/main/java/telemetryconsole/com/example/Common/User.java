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
        this.setUserDetails(userdetails);
        this.setAccessLevel(accessLevel);
    }

    public User(UserDetails userDetails) {
        this.setUserDetails(userDetails);
    }

    public User(Boolean errorUser) {
        if (errorUser) {
            this.setUserDetails(new UserDetails(DefaultStrings.ErrorUser, null));
            this.setAccessLevel(AccessLevel.INVALID);
        }
    }
}
