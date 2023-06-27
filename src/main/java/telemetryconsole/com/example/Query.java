package telemetryconsole.com.example;

import telemetryconsole.com.example.Common.QueryParameters;
import telemetryconsole.com.example.Common.QueryType;

public class Query {

    private QueryType queryType;
    private QueryParameters queryParameters;

    public QueryType getQueryType() {
        return queryType;
    }

    public QueryParameters getQueryParameters() {
        return queryParameters;
    }

    public void setQueryParameters(QueryParameters queryParameters) {
        this.queryParameters = queryParameters;
    }


    public Query(QueryType selectedQueryType, QueryParameters params) {
        
        queryType = selectedQueryType;
        
        if (params != null) {
            setQueryParameters(params);
        }


        switch(queryType) {
            case QUERYDEVICE:
                break;
            
            default:
                System.out.println("Query type is not yet implemented!");
                break;
        } 
    }

    public QueryResults RunQuery() {
        // Create instance of DataConnector (update operation) or use static instance
        // Query data source
        // Parse results
        
        // Create new instance of QueryResults
        QueryResults queryResults = new QueryResults();

        // Create new instance of QueryItem for each result and add to collection
        // Add collection to QueryResults
        // Return QueryResults

        return queryResults;
    }
}
