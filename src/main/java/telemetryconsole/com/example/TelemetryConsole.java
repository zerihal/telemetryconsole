package telemetryconsole.com.example;

import telemetryconsole.com.example.Common.AccessLevel;
import telemetryconsole.com.example.Common.DeviceParameters;
import telemetryconsole.com.example.Common.ParseDataException;
import telemetryconsole.com.example.Common.QueryParameters;
import telemetryconsole.com.example.Common.QueryType;
import telemetryconsole.com.example.Common.QueryValidator;
import telemetryconsole.com.example.Common.UnauthorisedUserException;
import telemetryconsole.com.example.Common.User;
import telemetryconsole.com.example.Util.StringHelper;

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

    public Boolean CanAuthenticate(String username, String password) {
        // If username or password have not been entered then display an error and return as authentication 
        // cannot be done. In the UI this would probably act to enable or disable a "login" or "authenticate"
        // button
        if (StringHelper.IsStringNullOrEmpty(username) || StringHelper.IsStringNullOrEmpty(password)) {
            EnableAuthenticateUser(false);
            return false;
        } else {
            EnableAuthenticateUser(true);
            return true;
        }
    }

    public void AuthenticateUser(String username, String password) {

        // Create instance of Authenticate - this can be reused should incorrect user details be entered
        // by updating the property in the instance and rerunning DoAuthentication
        set_authenticate(new Authenticate(username, password));

        // Set the user from the do authenticate operation
        set_currentUser(get_authenticate().DoAuthentication());

        // Do UI actions based on returned user - simplified below, but in reality this would perhaps take
        // the user to a new view if authenticated to be able to run queries and perform other actions, including admin
        // including admin functions if they have sufficient access to do so
        switch(get_currentUser().getAccessLevel()) {

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

    public Boolean CanRunQuery(QueryType queryType, QueryParameters queryParams) {
        
        Boolean canRunQuery = false;

        // Ensure current user is authenticated
        if (get_currentUser() != null) {
            AccessLevel cuAccess = get_currentUser().getAccessLevel();

            if (cuAccess == AccessLevel.ADMIN || cuAccess == AccessLevel.USER) {

                if (queryType == QueryType.QUERYDEVICE) {

                    // This is a query for a specific device so check that the device identifier is valid
                    if (queryParams != null && queryParams instanceof DeviceParameters) {
                        canRunQuery = QueryValidator.IsValidSerialNo(((DeviceParameters)queryParams).getDeviceIdentifier());
                    }           
                }
                else {
                    // Placeholder for other query types
                }
            }
        }

        EnableRunQuery(canRunQuery);
        return canRunQuery;
    }

    public void RunQuery(QueryType queryType, QueryParameters queryParams) throws ParseDataException, UnauthorisedUserException {
        
        // Create Query instance
        Query query = new Query(queryType, queryParams);

        // Execute the query and set results
        set_currentQueryResults(query.ExecuteQuery());

        // Placeholder - further UI actions (if appropriate here)
    }

    private void EnableRunQuery(Boolean canRunQuery) {
        // Placeholder - enable the run query button
    }

    private void EnableAuthenticateUser(Boolean canAuthenticate) {
        // Placeholder - enable the authenticate / login button
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
