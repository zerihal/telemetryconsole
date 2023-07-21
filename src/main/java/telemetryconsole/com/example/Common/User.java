package telemetryconsole.com.example.Common;

public class User {
    private UserDetails _userDetails;
    private AccessLevel _accessLevel;

    public UserDetails get_userDetails() {
        return _userDetails;
    }

    public AccessLevel get_accessLevel() {
        return _accessLevel;
    }

    public User(UserDetails userdetails, AccessLevel accessLevel) {    
        _userDetails = userdetails;
        _accessLevel = accessLevel;
    }

    public User(Boolean errorUser) {
        if (errorUser) {
            _userDetails = new UserDetails(DefaultStrings.ErrorUser, null);
            _accessLevel = AccessLevel.INVALID;
        }
    }
}
