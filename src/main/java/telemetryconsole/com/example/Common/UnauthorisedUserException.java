package telemetryconsole.com.example.Common;

public class UnauthorisedUserException extends Exception {
    private AccessLevel _userAccessLevel;

    public AccessLevel getUserAccessLevel() {
        return _userAccessLevel;
    }

    public UnauthorisedUserException(AccessLevel accessLevel) {
        super("Unauthorised user exception");
        _userAccessLevel = accessLevel;
    }
}
