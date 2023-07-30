package telemetryconsole.com.example;

import telemetryconsole.com.example.Common.AccessLevel;
import telemetryconsole.com.example.Common.DeviceParameters;
import telemetryconsole.com.example.Common.InvalidUserDetailsException;
import telemetryconsole.com.example.Common.ParseDataException;
import telemetryconsole.com.example.Common.QueryParameters;
import telemetryconsole.com.example.Common.QueryType;
import telemetryconsole.com.example.Common.QueryValidator;
import telemetryconsole.com.example.Common.User;
import telemetryconsole.com.example.Util.StringHelper;

public class TelemetryConsole {
    
    private User currentUser;
    private Authenticate authenticate;
    private QueryResults currentQueryResults;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public Authenticate getAuthenticate() {
        return authenticate;
    }

    public void setAuthenticate(Authenticate _authenticate) {
        this.authenticate = _authenticate;
    }

    public QueryResults getCurrentQueryResults() {
        return currentQueryResults;
    }

    public void setCurrentQueryResults(QueryResults _currentQueryResults) {
        this.currentQueryResults = _currentQueryResults;
    }

    public TelemetryConsole() {}

    public void authenticateUser(String username, String password) throws InvalidUserDetailsException {

        // Create instance of Authenticate - this can be reused should incorrect user details be entered
        // by updating the property in the instance and rerunning DoAuthentication
        setAuthenticate(new Authenticate(username, password));

        // Set the user from the do authenticate operation (which returns a new instance of User)
        setCurrentUser(getAuthenticate().doAuthentication());

        // Do UI actions based on returned user - simplified below, but in reality this would perhaps take
        // the user to a new view if authenticated to be able to run queries and perform other actions, including admin
        // including admin functions if they have sufficient access to do so
        switch(getCurrentUser().getAccessLevel()) {

            case USER:
                displayLoggedOnUser();
                break;

            case ADMIN:
                displayLoggedOnUser();
                // Placeholder - Display admin options on console
                break;

            case NONE:
                incorrectUserPasswordError();
                break;

            case INVALID:
                invalidUserError();
                break;

            default:
                break;
        }
    }

    public void runQuery(QueryType queryType, QueryParameters queryParams) throws ParseDataException {
        
        // Create Query instance
        Query query = new Query(queryType, queryParams);

        // Execute the query and set results
        setCurrentQueryResults(query.executeQuery());

        // Placeholder - further UI actions (if appropriate here)
    }

    // Placeholder method for the UI to enable/disable login button
    @SuppressWarnings("unused")
    private boolean canAuthenticate(String username, String password) {

        if (StringHelper.isStringNullOrEmpty(username) || StringHelper.isStringNullOrEmpty(password)) {
            enableAuthenticateUser(false);
            return false;
        } else {
            enableAuthenticateUser(true);
            return true;
        }
    }

    // Placeholder method for UI to enable/disable run query button(s)
    @SuppressWarnings("unused")
    private boolean canRunQuery(QueryType queryType, QueryParameters queryParams) {
        
        boolean canRunQuery = false;

        // Ensure current user is authenticated
        if (getCurrentUser() != null) {
            AccessLevel cuAccess = getCurrentUser().getAccessLevel();

            if (cuAccess == AccessLevel.ADMIN || cuAccess == AccessLevel.USER) {

                if (queryType == QueryType.QUERYDEVICE) {

                    // This is a query for a specific device so check that the device identifier is valid
                    if (queryParams != null && queryParams instanceof DeviceParameters) {
                        canRunQuery = QueryValidator.isValidSerialNo(((DeviceParameters)queryParams).getDeviceIdentifier());
                    }           
                }
                else {
                    // Placeholder for other query types
                }
            }
        }

        enableRunQuery(canRunQuery);
        return canRunQuery;
    }

    private void enableRunQuery(Boolean canRunQuery) {
        // Placeholder - enable the run query button
    }

    private void enableAuthenticateUser(Boolean canAuthenticate) {
        // Placeholder - enable the authenticate / login button
    }

    private void displayLoggedOnUser() {
        // Placeholder - this is to display the logged on user in the UI (for now just output though)
        System.out.println("Logged on user: " + getCurrentUser().getUserDetails().getUsername());
    }

    private void incorrectUserPasswordError() {
        // Placeholder - this is to display incorrect password error/prompt in the UI (for now just output though)
        System.out.println("Incorrect password!");
    }

    private void invalidUserError() {
        // Placeholder - this is to display invalid user warning/prompt in the UI (for now just output though)
        System.out.println("Invalid user!");
    }
}
