package telemetryconsole.com.example;

import telemetryconsole.com.example.Common.AccessLevel;
import telemetryconsole.com.example.Common.UserDetails;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        // NOTE: SQLITE is not secure, but should in theory only be visible to the application
        // This should be moved into the application context (i.e. a folder created in here for it)
        // if just retained for testing purposes, but maybe consider alternate DB option

        System.out.println("Device Telemetry Console");

        //Sandbox.CreateNewDatabase("consoleUsers.db");
        //Sandbox.CreateNewUsersTable("consoleUsers.db");
        //Sandbox.InsertUser("consoleUsers.db", "jsweet", "password1", 2);
        Sandbox.SelectAllUsers("consoleUsers.db");

        Authenticate authenticate = new Authenticate();
        UserDetails userDetails = new UserDetails("jsweet", "password1");
        AccessLevel accessLevel = authenticate.checkUser(userDetails);
        
        System.out.println("User access level = " + accessLevel);
    }
}
