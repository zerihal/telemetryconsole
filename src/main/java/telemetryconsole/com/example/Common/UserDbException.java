package telemetryconsole.com.example.Common;

public class UserDbException extends Exception {

    private UserDetails userDetails;
    
    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public UserDbException(){}

    public UserDbException(UserDetails userDetails, Exception exception) {
        super(exception);
        setUserDetails(userDetails);    
    }
}
