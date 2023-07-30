package telemetryconsole.com.example;

import java.util.ArrayList;
import java.util.List;

import telemetryconsole.com.example.Common.DataConnector;

public class QueryResults {
    private int numberOfItemsPerPage;
    private List<QueryItem> queryItems;
    private DataConnector dataConnector;

    public int getNumberOfItemsPerPage() {
        return numberOfItemsPerPage;
    }

    public List<QueryItem> getQueryItems() {
        return queryItems;
    }

    public DataConnector getDataConnector() {
        return dataConnector;
    }

    public QueryResults(DataConnector dataConnector) {
        this.dataConnector = dataConnector;
        this.queryItems = new ArrayList<QueryItem>();
    }

    public QueryResults(DataConnector dataConnector, List<QueryItem> devices) {
        this.dataConnector = dataConnector;
        this.queryItems = devices;
    }
}
