package telemetryconsole.com.example.Common;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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

    public Object connectDataDB() {
        // Placeholder method only at the moment
        return null;
    }

    public Object getDeviceDataAPI() {
        // Placeholder method only at the moment
        return null;
    }

    public Object getAppDataAPI() {
        // Placeholder method only at the moment
        return null;       
    }

    public ArrayList<Object[]> getData(QueryType queryType, QueryParameters queryParams) {
        
        ArrayList<Object[]> rawData = new ArrayList<Object[]>();
        String dbQuery = null;
        String deviceDataBaseQuery = "SELECT id, datelogged, deviceidentifier, devicename, devicetypename, devicestatus FROM loggeddata";

        switch(queryType) {
            case QUERYDEVICE:
                if (queryParams != null && queryParams instanceof DeviceParameters) {
                    DeviceParameters devParams = (DeviceParameters)queryParams;
                    dbQuery = deviceDataBaseQuery + " WHERE deviceidentifier = '" + devParams.getDeviceIdentifier() + "'";
                } else {
                    // Device query type has been specified, however without valid DeviceParameters. Throw an exception
                    // as this is an error!
                }
                break;

            case QUERYALLDEVICES:
            case QUERYTRIPDATA:
            case QUERYAPPDATA:           
            default:
                System.out.println("Query type not yet implemented!");
                break;
        } 

        System.out.println(dbQuery);

        if (dbQuery != null) { 
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
        }

        return rawData;
    }
}
