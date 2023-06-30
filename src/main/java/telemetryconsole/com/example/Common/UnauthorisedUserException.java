package telemetryconsole.com.example.Common;

public class UnauthorisedUserException extends Exception {
    private AccessLevel _userAccessLevel;

    public AccessLevel getUserAccessLevel() {
        return _userAccessLevel;
    }

    public void setUserAccessLevel(AccessLevel _userAccessLevel) {
        this._userAccessLevel = _userAccessLevel;
    }

    public UnauthorisedUserException(AccessLevel accessLevel) {
        setUserAccessLevel(accessLevel);
    }
}
