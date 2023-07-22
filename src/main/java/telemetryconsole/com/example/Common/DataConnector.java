package telemetryconsole.com.example.Common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import telemetryconsole.com.example.Common.QueryFilters.QueryFilter;

// This is for connecting to the sample SQLite DB only at the moment, but in full implementation, this
// class should be responsible for establishing a connection to various data sources required to obtain
// telemetry data - likely to be one or more web based API and/or remote database. Skeleton methods for
// this functionality has been added as placeholders.

public class DataConnector {

    public Connection connect() {
        // SQLite connection string
        String url = DefaultStrings.SQLiteDBPath + DefaultStrings.DeviceDataDB;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public Object ConnectDataDB() {
        // Placeholder method only at the moment - to return database connection
        return null;
    }

    public Object GetDeviceDataAPI() {
        // Placeholder method only at the moment - to return device data API connector
        return null;
    }

    public Object GetAppDataAPI() {
        // Placeholder method only at the moment - to return app data API connector
        return null;       
    }

    public ArrayList<Object[]> GetData(QueryType queryType, QueryParameters queryParams, QueryFilter queryFilter) {
        
        ArrayList<Object[]> rawData = new ArrayList<Object[]>();
        String dbQuery = null;

        // Create a base query string - this can then be appended with specifics for the query type, parameters and filters
        // as require
        String deviceDataBaseQuery = "SELECT id, datelogged, deviceidentifier, devicename, devicetypename, devicestatus FROM loggeddata";

        switch(queryType) {
            case QUERYDEVICE:
                DeviceParameters devParams = (DeviceParameters)queryParams;
                dbQuery = deviceDataBaseQuery + " WHERE deviceidentifier = '" + devParams.getDeviceIdentifier() + "'";
                break;

            case QUERYALLDEVICES:
            case QUERYTRIPDATA:
            case QUERYAPPDATA:           
            default:
                System.out.println("Query type not yet implemented!");
                break;
        }

        // ToDo: Apply additional query filters if applicable (not yet implemented)
        if (queryFilter != null)
        {
            // ...
        }

        if (dbQuery != null) { 

            if (queryType == QueryType.QUERYDEVICE || queryType == QueryType.QUERYALLDEVICES)
            {
                try (Connection conn = connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(dbQuery)) {

                    while (rs.next()) {
                        rawData.add(new Object[] {
                            rs.getString("datelogged"),
                            rs.getString("deviceidentifier"),
                            rs.getString("devicename"),
                            rs.getString("devicetypename"),
                            rs.getInt("devicestatus")
                        });
                    }

                    stmt.close();
                    conn.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }             
            } else if (queryType == QueryType.QUERYTRIPDATA) {
                // Not yet implemented
            } else if (queryType == QueryType.QUERYAPPDATA) {
                // Not yet implemented
            }           
        }

        return rawData;
    }
}
