package telemetryconsole.com.example;

import telemetryconsole.com.example.Common.ParseDataException;
import telemetryconsole.com.example.Common.QueryParameters;
import telemetryconsole.com.example.Common.QueryType;
import telemetryconsole.com.example.Common.UnauthorisedUserException;
import telemetryconsole.com.example.Common.User;
import telemetryconsole.com.example.Common.AccessLevel;

/*
import telemetryconsole.com.SampleSetup.Sandbox;
import telemetryconsole.com.example.Common.DefaultStrings;
import telemetryconsole.com.example.Common.DeviceParameters;
import telemetryconsole.com.example.Common.ParseDataException;
import telemetryconsole.com.example.Common.QueryType;
import telemetryconsole.com.SampleSetup.SetupSampleData;
import telemetryconsole.com.SampleSetup.SetupSampleUsers;
import telemetryconsole.com.example.Common.UserDetails;
*/

/**
 * Telemetry Console
 *
 */
public class TelemetryConsole 
{
    private static User _currentUser;
    private static Authenticate _authenticate;
    private static Query _currentQuery;
    private static QueryResults _currentQueryResults;

    public static User get_currentUser() {
        return _currentUser;
    }

    public static void set_currentUser(User user) {
        TelemetryConsole._currentUser = user;
    }

    public static Authenticate get_authenticate() {
        return _authenticate;
    }

    public static void set_authenticate(Authenticate _authenticate) {
        TelemetryConsole._authenticate = _authenticate;
    }

    public static Query get_currentQuery() {
        return _currentQuery;
    }

    public static void set_currentQuery(Query _currentQuery) {
        TelemetryConsole._currentQuery = _currentQuery;
    }

    public static QueryResults get_currentQueryResults() {
        return _currentQueryResults;
    }

    public static void set_currentQueryResults(QueryResults _currentQueryResults) {
        TelemetryConsole._currentQueryResults = _currentQueryResults;
    }

    public static void main( String[] args )
    {
        // NOTE: SQLITE is not secure, but should in theory only be visible to the application
        // This should be moved into the application context (i.e. a folder created in here for it)
        // if just retained for testing purposes, but maybe consider alternate DB option

        System.out.println("Device Telemetry Console");

        //SetupSampleUsers setupSampleUsers = new SetupSampleUsers();
        //setupSampleUsers.RunSetup();

        // NOTE: THE FOLLOWING IS TEST CODE - THIS IS TO BE REMOVED
        /*
        UserDetails userDetails = new UserDetails("jblogs", "password1");

        Authenticate authenticate = new Authenticate(userDetails);

        setCurrentUser(authenticate.AuthenticateUser());
        
        System.out.println("User access level = " + getCurrentUser().getAccessLevel());

        String deviceIdent = "SRSU4T51ZK";

        Query query = new Query(QueryType.QUERYDEVICE, new DeviceParameters(deviceIdent));
        QueryResults results = null;

        try {
            results = query.RunQuery();
        } catch (ParseDataException e) {

        }

        if (results != null) {
            int queryItemCount = results.getQueryItems().size();
            System.out.println(queryItemCount);
        }
        */

        //SetupSampleData setupSampleData = new SetupSampleData();
        //setupSampleData.RunSetup();
    }

    public static void AuthenticateUser(String username, String password) {
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

    public static void RunQuery(QueryType queryType, QueryParameters queryParams) throws ParseDataException, UnauthorisedUserException {
        
        if (get_currentUser() == null) {
            return;
        }

        AccessLevel cuAccess = get_currentUser().getAccessLevel();

        if (!(cuAccess == AccessLevel.ADMIN || cuAccess == AccessLevel.USER)) {
            throw new UnauthorisedUserException(cuAccess);
        }

        set_currentQuery(new Query(queryType, queryParams));
        set_currentQueryResults(get_currentQuery().RunQuery());
    }

    private static void DisplayLoggedOnUser() {
        // Placeholder - this is to display the logged on user in the UI (for now just output though)
        System.out.println("Logged on user: " + get_currentUser().getUserDetails().getUsername());
    }

    private static void IncorrectUserPasswordError() {
        // Placeholder - this is to display incorrect password error/prompt in the UI (for now just output though)
        System.out.println("Incorrect password!");
    }

    private static void InvalidUserError() {
        // Placeholder - this is to display invalid user warning/prompt in the UI (for now just output though)
        System.out.println("Invalid user!");
    }
}
