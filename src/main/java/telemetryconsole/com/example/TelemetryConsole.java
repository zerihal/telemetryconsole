package telemetryconsole.com.example;

import telemetryconsole.com.SampleSetup.Sandbox;
import telemetryconsole.com.example.Common.DefaultStrings;
import telemetryconsole.com.example.Common.DeviceParameters;
import telemetryconsole.com.example.Common.ParseDataException;
import telemetryconsole.com.example.Common.QueryType;
//import telemetryconsole.com.SampleSetup.SetupSampleData;
//import telemetryconsole.com.SampleSetup.SetupSampleUsers;
import telemetryconsole.com.example.Common.User;
import telemetryconsole.com.example.Common.UserDetails;

/**
 * Hello world!
 *
 */
public class TelemetryConsole 
{
    private static User currentUser;
   
    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        TelemetryConsole.currentUser = user;
    }

    public static void main( String[] args )
    {
        // NOTE: SQLITE is not secure, but should in theory only be visible to the application
        // This should be moved into the application context (i.e. a folder created in here for it)
        // if just retained for testing purposes, but maybe consider alternate DB option

        System.out.println("Device Telemetry Console");

        //SetupSampleUsers setupSampleUsers = new SetupSampleUsers();
        //setupSampleUsers.RunSetup();

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
        
        //SetupSampleData setupSampleData = new SetupSampleData();
        //setupSampleData.RunSetup();
    }
}
