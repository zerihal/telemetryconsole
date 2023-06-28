package telemetryconsole.com.SampleSetup;

import telemetryconsole.com.example.Common.DefaultStrings;

public class SetupSampleUsers implements ISetupSample {
    
    @Override
    public void RunSetup() {

        // ToDo: If DB already exists then delete it or ask user to confirm

        // Create the database and a new users table
        System.out.println("Creating users DB and table");
        Sandbox.CreateNewDatabase(DefaultStrings.ConsoleUsersDB);

        String sql = "CREATE TABLE IF NOT EXISTS users (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	username text NOT NULL,\n"
                + "	password text NOT NULL,\n"
                + " accesslevel integer NOT NULL\n"
                + ");";

        Sandbox.CreateNewTable(DefaultStrings.ConsoleUsersDB, sql);

        // Insert some sample users into the table
        System.out.println("Adding sample users");
        Sandbox.InsertUser(DefaultStrings.ConsoleUsersDB, "jblogs", "password1", 2);
        Sandbox.InsertUser(DefaultStrings.ConsoleUsersDB, "jbiggs", "password2", 1);
        Sandbox.InsertUser(DefaultStrings.ConsoleUsersDB, "bFish", "password3", 1);
        Sandbox.InsertUser(DefaultStrings.ConsoleUsersDB, "aNon", "password4", 0);

        // Output all users in new DB table
        System.out.println("Sample users DB created - the following users have been added:");
        Sandbox.SelectAllUsers(DefaultStrings.ConsoleUsersDB);
    }
}
