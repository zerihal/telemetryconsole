package telemetryconsole.com.example;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import telemetryconsole.com.example.Common.DataConnector;
import telemetryconsole.com.example.Common.DeviceStatus;
import telemetryconsole.com.example.Common.ParseDataException;
import telemetryconsole.com.example.Common.QueryParameters;
import telemetryconsole.com.example.Common.QueryType;

public class Query {

    private QueryType _queryType;
    private QueryParameters _queryParameters;
    private DataConnector _dataConnector;

    public QueryType getQueryType() {
        return _queryType;
    }

    public QueryParameters getQueryParameters() {
        return _queryParameters;
    }

    public void setQueryParameters(QueryParameters queryParameters) {
        this._queryParameters = queryParameters;
    }


    public Query(QueryType selectedQueryType, QueryParameters params) {
        
        _queryType = selectedQueryType;
        
        if (params != null) {
            setQueryParameters(params);
        }
    }

    public QueryResults RunQuery() throws ParseDataException {

        // Create instance of DataConnector (update operation) or use static instance
        _dataConnector = new DataConnector();

        // Query data source
        ArrayList<Object[]> dbQueryResultsRaw = _dataConnector.getData(getQueryType(), getQueryParameters());

        // Parse results to obtain collection of Device objects
        ArrayList<QueryItem> devices = null;
        
        try {
            devices = ParseData(dbQueryResultsRaw);
        } catch (ParseException e) {
            throw new ParseDataException();
        } catch (ParseDataException parseDataException) {
            throw parseDataException;
        }
        
        // Create new instance of QueryResults
        QueryResults queryResults = new QueryResults(devices);

        // Return QueryResults
        return queryResults;
    }

    private ArrayList<QueryItem> ParseData(ArrayList<Object[]> resultSet) throws ParseException, ParseDataException {

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
