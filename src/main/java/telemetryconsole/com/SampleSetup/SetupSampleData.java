package telemetryconsole.com.SampleSetup;

import java.util.Random;

import telemetryconsole.com.example.Common.DefaultStrings;
import telemetryconsole.com.example.Util.FileHelper;

public class SetupSampleData implements ISetupSample {

    private static Random rand = new Random();

    @Override
    public void runSetup() {
        
        // If DB already exists then delete it
        if (FileHelper.deleteFile(DefaultStrings.WindowsSQLiteDbPath() + DefaultStrings.DeviceDataDB)) {

            System.out.println("Existing sample DB found and deleted or did not exist");

            // Create the database and a new device data table
            System.out.println("Creating device data DB and table");
            Sandbox.createNewDatabase(DefaultStrings.DeviceDataDB);
            
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

            Sandbox.createNewTable(DefaultStrings.DeviceDataDB, sql);

            // Generate and insert some sample data to the DB
            String deviceType = "ESP32-100";

            for (int i = 0; i < 10; i++) {
                String deviceName = "TestDrive_" + String.valueOf(i);
                String deviceId = generateSerialNo();

                // Create 10 sample entries for the device
                for (int i2 = 0; i2 < 10; i2++) {
                    String randomDateTime = getDateTimeString();
                    int randomStatus = generateStatus();

                    Sandbox.insertDeviceData(DefaultStrings.DeviceDataDB, 
                        randomDateTime, deviceId, deviceName, deviceType, randomStatus);
                }
            }

            System.out.println("Sample device data DB created - the following entries have been added:");
        } else {
            System.out.println("Existing sample DB found but could not be deleted - this contains the following:");
        }

        // Output the data logged
        Sandbox.selectAllDevices(DefaultStrings.DeviceDataDB);
    }
    
    private static String getDateTimeString() {
        String day = String.format("%02d", rand.nextInt(29) + 1);
        String month = String.format("%02d",rand.nextInt(1) + 5);
        String year = "2023";
        String hour = String.format("%02d",rand.nextInt(7) + 14);
        String min = String.format("%02d",rand.nextInt(59));
        String sec = String.format("%02d",rand.nextInt(59));

        return day + "/" + month  + "/" + year + " " + hour + ":" + min + ":" + sec;
    }

    private static String generateSerialNo() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVXYZ0123456789";

        String serialNo = "S";

        for (int i = 0; i < 9; i++) {
            char ranChar = chars.charAt(rand.nextInt(33));
            serialNo += ranChar;
        }

        return serialNo;
    }

    private static int generateStatus() {
        // Status 0 is Healthy
        // Status 1 is Tripped
        // Status 2 is Fault
        // Status 3 is Unknown

        int seed = rand.nextInt(12);

        if (seed > 7 && seed < 10) {
            return 1;
        } else if (seed == 10) {
            return 2;
        } else if (seed == 11) {
            return 3;
        } else {
            return 0;
        }
    }
}
