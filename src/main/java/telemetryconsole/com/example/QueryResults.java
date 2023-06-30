package telemetryconsole.com.example;

import java.util.ArrayList;
import java.util.List;

public class QueryResults {
    private int _numberOfItemsPerPage;
    private List<QueryItem> _queryItems;

    public int getNumberOfItemsPerPage() {
        return _numberOfItemsPerPage;
    }

    public List<QueryItem> getQueryItems() {
        return _queryItems;
    }

    public QueryResults() {
        _queryItems = new ArrayList<QueryItem>();
    }

    public QueryResults(List<QueryItem> devices) {
        _queryItems = devices;
    }
}
