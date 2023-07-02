package telemetryconsole.com.example;

import telemetryconsole.com.example.Common.AccessLevel;
import telemetryconsole.com.example.Common.ParseDataException;
import telemetryconsole.com.example.Common.QueryParameters;
import telemetryconsole.com.example.Common.QueryType;
import telemetryconsole.com.example.Common.UnauthorisedUserException;
import telemetryconsole.com.example.Common.User;

public class TelemetryConsole {
    
    private User _currentUser;
    private Authenticate _authenticate;
    private QueryResults _currentQueryResults;

    public User get_currentUser() {
        return _currentUser;
    }

    public void set_currentUser(User user) {
        this._currentUser = user;
    }

    public Authenticate get_authenticate() {
        return _authenticate;
    }

    public void set_authenticate(Authenticate _authenticate) {
        this._authenticate = _authenticate;
    }

    public QueryResults get_currentQueryResults() {
        return _currentQueryResults;
    }

    public void set_currentQueryResults(QueryResults _currentQueryResults) {
        this._currentQueryResults = _currentQueryResults;
    }

    public TelemetryConsole() {}

    public void AuthenticateUser(String username, String password) {
        set_authenticate(new Authenticate(username, password));

        User user = get_authenticate().AuthenticateUser();
        set_currentUser(user);

        switch(user.getAccessLevel()) {

            case USER:
                DisplayLoggedOnUser();
                break;

            case ADMIN:
                DisplayLoggedOnUser();
                // Placeholder - Display admin options on console
                break;

            case NONE:
                IncorrectUserPasswordError();
                break;

            case INVALID:
                InvalidUserError();
                break;

            default:
                break;
        }
    }

    public void RunQuery(QueryType queryType, QueryParameters queryParams) throws ParseDataException, UnauthorisedUserException {
        
        if (get_currentUser() == null) {
            return;
        }

        AccessLevel cuAccess = get_currentUser().getAccessLevel();

        if (!(cuAccess == AccessLevel.ADMIN || cuAccess == AccessLevel.USER)) {
            throw new UnauthorisedUserException(cuAccess);
        }

        Query query = new Query(queryType, queryParams);
        set_currentQueryResults(query.RunQuery());
    }

    private void DisplayLoggedOnUser() {
        // Placeholder - this is to display the logged on user in the UI (for now just output though)
        System.out.println("Logged on user: " + get_currentUser().getUserDetails().getUsername());
    }

    private void IncorrectUserPasswordError() {
        // Placeholder - this is to display incorrect password error/prompt in the UI (for now just output though)
        System.out.println("Incorrect password!");
    }

    private void InvalidUserError() {
        // Placeholder - this is to display invalid user warning/prompt in the UI (for now just output though)
        System.out.println("Invalid user!");
    }
}
