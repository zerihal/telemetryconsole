package telemetryconsole.com.example.TestHelpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import telemetryconsole.com.example.Device;
import telemetryconsole.com.example.QueryItem;
import telemetryconsole.com.example.Common.DeviceStatus;
import telemetryconsole.com.example.Common.ParseDataException;

public class TestHelper {

        /**
        * Copy of the internal ParseDeviceData method in Query for testing purposes.
        */
        public static ArrayList<QueryItem> ParseDeviceData(ArrayList<Object[]> resultSet) throws ParseException, ParseDataException {

        ArrayList<QueryItem> devices = new ArrayList<QueryItem>();
        SimpleDateFormat dtFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        for (Object[] dataEntry : resultSet) {

            Date dateLogged = dtFormatter.parse((String)dataEntry[0]);
            DeviceStatus devStatus = DeviceStatus.values()[(int)dataEntry[4]];

            Device device = new Device(
                dateLogged, 
                (String)dataEntry[1], 
                (String)dataEntry[2], 
                (String)dataEntry[3], 
                devStatus
                );
            
            devices.add(device);
        }

        return devices;
    }
}
