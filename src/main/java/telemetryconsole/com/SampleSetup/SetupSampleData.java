package telemetryconsole.com.SampleSetup;

import java.util.Random;

import telemetryconsole.com.example.Common.DefaultStrings;

public class SetupSampleData implements ISetupSample {

    private static Random rand = new Random();

    @Override
    public void RunSetup() {
        
        // Create the database and a new device data table
        System.out.println("Creating users DB and table");
        Sandbox.CreateNewDatabase(DefaultStrings.DeviceDataDB);
        
        // Create SQL query for new table. For this example we'll allow the detailed
        // device data to be null, however in reality most of these (other than maybe
        // trip data) would not be null. The data types have also been simplified for
        // SQLite, but again, in reality this would be in a full SQL DB with a larger
        // range of data types available.
        String sql = "CREATE TABLE IF NOT EXISTS loggeddata (\n"
        + "	id integer PRIMARY KEY,\n"
        + "	datelogged text NOT NULL,\n"
        + "	deviceidentifier text NOT NULL,\n"
        + "	devicename text NOT NULL,\n"
        + "	devicetypename text NOT NULL,\n"
        + "	devicestatus integer NOT NULL,\n"
        + "	devicetype real,\n"
        + "	menu1 text,\n"
        + "	menu2 text,\n"
        + "	menu3 text,\n"
        + "	menu4 text,\n"
        + "	menu5 text,\n"
        + "	tripdata text\n"
        + ");";

        Sandbox.CreateNewTable(DefaultStrings.ConsoleUsersDB, sql);

        // Generate and insert some sample data to the DB
        for (int i = 0; i < 100; i++) {

        }
    }
    
    private static String GetDateTimeString() {
        String day = String.valueOf(rand.nextInt(29) + 1);
        String month = String.valueOf(rand.nextInt(1) + 5);
        String year = "2023";
        String hour = String.valueOf(rand.nextInt(7) + 14);
        String min = String.valueOf(rand.nextInt(59));
        String sec = String.valueOf(rand.nextInt(59));

        return day + ":" + month  + ":" + year + " " + hour + ":" + min + ":" + sec;
    }
}
