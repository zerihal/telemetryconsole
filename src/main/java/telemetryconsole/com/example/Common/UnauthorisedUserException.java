package telemetryconsole.com.example.Common;

public class UnauthorisedUserException extends Exception {
    private AccessLevel userAccessLevel;

    public AccessLevel getUserAccessLevel() {
        return userAccessLevel;
    }

    public UnauthorisedUserException(AccessLevel accessLevel) {
        super("Unauthorised user exception");
        this.userAccessLevel = accessLevel;
    }
}
