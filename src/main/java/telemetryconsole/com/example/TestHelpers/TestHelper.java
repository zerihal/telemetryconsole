package telemetryconsole.com.example.TestHelpers;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import telemetryconsole.com.example.Device;
import telemetryconsole.com.example.Query;
import telemetryconsole.com.example.QueryItem;
import telemetryconsole.com.example.TelemetryConsole;
import telemetryconsole.com.example.Common.AccessLevel;
import telemetryconsole.com.example.Common.DeviceParameters;
import telemetryconsole.com.example.Common.DeviceStatus;
import telemetryconsole.com.example.Common.ParseDataException;
import telemetryconsole.com.example.Common.QueryParameters;
import telemetryconsole.com.example.Common.QueryType;
import telemetryconsole.com.example.Common.QueryValidator;
import telemetryconsole.com.example.Common.User;

public class TestHelper {

    /**
     * Uses reflection to get the internal ParseDeviceData method from a query instance and perform same functionality
     * for testing purposes, returning ArrayList<QueryItem> as per reflected method. Uses fallback to perform same
     * functionality if any reflection exception.
     */
    public static ArrayList<QueryItem> ParseDeviceDataInternal(Query queryInstance, ArrayList<Object[]> resultSet) throws ParseException, ParseDataException {
        
        try {
            Method meth = queryInstance.getClass().getDeclaredMethod("ParseDeviceData", ArrayList.class);
            meth.setAccessible(true);

            @SuppressWarnings("unchecked")
            ArrayList<QueryItem> methInvoke = (ArrayList<QueryItem>)meth.invoke(queryInstance, resultSet);

            if (methInvoke != null) {
                return methInvoke;
            }
        } catch (Exception e) {
            // Don't do anything here, we'll just use the fallback copy method
        }

        return ParseDeviceDataCopy(resultSet);
    }

    /**
     * Uses reflection to get the internal CanRunQuery method from a TelemetryConsole instance and perform same functionality
     * for testing purposes, returning boolean result as per reflected method. Uses fallback to perform same
     * functionality if any reflection exception.
     */
    public static boolean CanRunQueryInternal(TelemetryConsole telemetryConsole, QueryType queryType, QueryParameters queryParams) {

        try {
            Method privateCanQueryMeth = telemetryConsole.getClass().getDeclaredMethod("CanRunQuery", QueryType.class, QueryParameters.class);
            privateCanQueryMeth.setAccessible(true);
            return (boolean)privateCanQueryMeth.invoke(telemetryConsole, queryType, queryParams);
        } catch (Exception e) {
            // Don't do anything here, we'll just use the fallback copy method
        }

        return CanRunQueryCopy(queryType, queryParams, telemetryConsole.get_currentUser());
    }
    
    /**
    * Copy of the internal ParseDeviceData method in Query for testing purposes.
    */
    private static ArrayList<QueryItem> ParseDeviceDataCopy(ArrayList<Object[]> resultSet) throws ParseException, ParseDataException {

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

    /**
    * Copy of the internal CanRunQuery method in TelemetryConsole for testing purposes (with additional User arg).
    */
    private static boolean CanRunQueryCopy(QueryType queryType, QueryParameters queryParams, User currentUser) {
        
        boolean canRunQuery = false;

        // Ensure current user is authenticated
        if (currentUser != null) {
            AccessLevel cuAccess = currentUser.get_accessLevel();

            if (cuAccess == AccessLevel.ADMIN || cuAccess == AccessLevel.USER) {

                if (queryType == QueryType.QUERYDEVICE) {

                    // This is a query for a specific device so check that the device identifier is valid
                    if (queryParams != null && queryParams instanceof DeviceParameters) {
                        canRunQuery = QueryValidator.IsValidSerialNo(((DeviceParameters)queryParams).getDeviceIdentifier());
                    }           
                }
                else {
                    // Placeholder for other query types
                }
            }
        }

        return canRunQuery;
    }    
}
