package telemetryconsole.com.example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import telemetryconsole.com.example.Common.DataConnector;
import telemetryconsole.com.example.Common.DeviceStatus;
import telemetryconsole.com.example.Common.ParseDataException;
import telemetryconsole.com.example.Common.QueryParameters;
import telemetryconsole.com.example.Common.QueryType;
import telemetryconsole.com.example.Common.QueryFilters.QueryFilter;

public class Query {

    private QueryType _queryType;
    private QueryParameters _queryParameters;
    private DataConnector _dataConnector;

    private QueryFilter _queryFilter;

    public DataConnector get_dataConnector() {
        return _dataConnector;
    }

    public void set_dataConnector(DataConnector _dataConnector) {
        this._dataConnector = _dataConnector;
    }

    public QueryType getQueryType() {
        return _queryType;
    }

    public QueryParameters getQueryParameters() {
        return _queryParameters;
    }

    public void setQueryParameters(QueryParameters queryParameters) {
        this._queryParameters = queryParameters;
    }

    public QueryFilter get_queryFilter() {
        return _queryFilter;
    }

    public void set_queryFilter(QueryFilter _queryFilter) {
        this._queryFilter = _queryFilter;
    }

    public Query(QueryType selectedQueryType, QueryParameters params) {
        
        _queryType = selectedQueryType;
        
        if (params != null) {
            setQueryParameters(params);
        }
    }

    // Placeholder constructor for when QueryFilter implementation is done
    public Query(QueryType selectedQueryType, QueryParameters params, QueryFilter queryFilter) {
        
        this(selectedQueryType, params);
        set_queryFilter(queryFilter);
    }

    public QueryResults RunQuery() throws ParseDataException {

        // Create instance of DataConnector (update operation) or use static instance
        if (_dataConnector == null)
            _dataConnector = new DataConnector();

        // Query data source
        ArrayList<Object[]> dbQueryResultsRaw = get_dataConnector().getData(getQueryType(), getQueryParameters(), get_queryFilter());

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
        QueryResults queryResults = new QueryResults(get_dataConnector(), devices);

        // Return QueryResults
        return queryResults;
    }

    public ArrayList<QueryItem> ParseData(ArrayList<Object[]> resultSet) throws ParseException, ParseDataException {

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
