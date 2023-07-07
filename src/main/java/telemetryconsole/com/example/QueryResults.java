package telemetryconsole.com.example;

import java.util.ArrayList;
import java.util.List;

import telemetryconsole.com.example.Common.DataConnector;

public class QueryResults {
    private int _numberOfItemsPerPage;
    private List<QueryItem> _queryItems;
    private DataConnector _dataConnector;

    public int getNumberOfItemsPerPage() {
        return _numberOfItemsPerPage;
    }

    public List<QueryItem> getQueryItems() {
        return _queryItems;
    }

    public DataConnector get_dataConnector() {
        return _dataConnector;
    }

    public QueryResults(DataConnector dataConnector) {
        _dataConnector = dataConnector;
        _queryItems = new ArrayList<QueryItem>();
    }

    public QueryResults(DataConnector dataConnector, List<QueryItem> devices) {
        _dataConnector = dataConnector;
        _queryItems = devices;
    }
}
