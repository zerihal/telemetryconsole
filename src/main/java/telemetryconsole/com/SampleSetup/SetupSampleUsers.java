package telemetryconsole.com.SampleSetup;

import telemetryconsole.com.example.Common.DefaultStrings;
import telemetryconsole.com.example.Util.FileHelper;

public class SetupSampleUsers implements ISetupSample {
    
    @Override
    public void runSetup() {

        // If DB already exists then delete it
        if (FileHelper.deleteFile(DefaultStrings.WindowsSQLiteDbPath() + DefaultStrings.ConsoleUsersDB)) {

            System.out.println("Existing sample DB found and deleted or did not exist");

            // Create the database and a new users table
            System.out.println("Creating users DB and table");
            Sandbox.createNewDatabase(DefaultStrings.ConsoleUsersDB);

            String sql = "CREATE TABLE IF NOT EXISTS users (\n"
                    + "	id integer PRIMARY KEY,\n"
                    + "	username text NOT NULL,\n"
                    + "	password text NOT NULL,\n"
                    + " accesslevel integer NOT NULL\n"
                    + ");";

            Sandbox.createNewTable(DefaultStrings.ConsoleUsersDB, sql);

            // Insert some sample users into the table
            System.out.println("Adding sample users");
            Sandbox.insertUser(DefaultStrings.ConsoleUsersDB, "jblogs", "password1", 2);
            Sandbox.insertUser(DefaultStrings.ConsoleUsersDB, "jbiggs", "password2", 1);
            Sandbox.insertUser(DefaultStrings.ConsoleUsersDB, "bFish", "password3", 1);
            Sandbox.insertUser(DefaultStrings.ConsoleUsersDB, "aNon", "password4", 0);

            // Output all users in new DB table
            System.out.println("Sample users DB created - the following users have been added:");
        } else {
            System.out.println("Existing sample DB found but could not be deleted - this contains the following:");
        }

        Sandbox.selectAllUsers(DefaultStrings.ConsoleUsersDB);
    }
}
