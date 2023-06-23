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
