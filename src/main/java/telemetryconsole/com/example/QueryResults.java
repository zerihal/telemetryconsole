package telemetryconsole.com.example;

import java.util.ArrayList;
import java.util.List;

public class QueryResults {
    private int NumberOfItemsPerPage;
    private List<QueryItem> queryItems;

    public int getNumberOfItemsPerPage() {
        return NumberOfItemsPerPage;
    }

    public List<QueryItem> getQueryItems() {
        return queryItems;
    }

    public QueryResults() {
        queryItems = new ArrayList<QueryItem>();
    }
}
