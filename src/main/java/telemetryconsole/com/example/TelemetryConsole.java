package telemetryconsole.com.example;

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


        //SetupSampleData setupSampleData = new SetupSampleData();
        //setupSampleData.RunSetup();
    }
}
