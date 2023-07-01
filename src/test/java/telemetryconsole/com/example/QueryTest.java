package telemetryconsole.com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import telemetryconsole.com.SampleSetup.Sandbox;
import telemetryconsole.com.SampleSetup.SetupSampleData;
import telemetryconsole.com.example.Common.AccessLevel;
import telemetryconsole.com.example.Common.DataConnector;
import telemetryconsole.com.example.Common.DefaultStrings;
import telemetryconsole.com.example.Common.DeviceParameters;
import telemetryconsole.com.example.Common.QueryType;
import telemetryconsole.com.example.Common.User;
import telemetryconsole.com.example.Common.UserDetails;
import telemetryconsole.com.example.Util.StringHelper;

public class QueryTest {

    static User testUser;
    static DeviceParameters deviceParameters;
    static String testDeviceIdentifier;

    @BeforeAll
    static void setUp() throws Exception {

        // Setup sample device data DB
        SetupSampleData setupSampleData = new SetupSampleData();
        setupSampleData.RunSetup();

        // Create an authenticated test user to check preconditions
        testUser = new User(new UserDetails("bFish", "password3"), AccessLevel.USER);
        TelemetryConsole.AuthenticateUser(testUser.getUserDetails().getUsername(), testUser.getUserDetails().getPassword());

        // Create instance of DeviceParameters to pass to the query. As the data, including the device identifier,
        // in the sample device data DB is dynamically generated from the setup action above, we need to take one
        // from here. There are 100 entries in the sample DB so just take one at random
        testDeviceIdentifier = Sandbox.SelectRandomDeviceIdentifier(DefaultStrings.DeviceDataDB);

        // In case there was a problem, thow an exception here so that it does obscure the results from the actual
        // tests that are to be run
        if (StringHelper.IsStringNullOrEmpty(testDeviceIdentifier)) {
            throw new Exception("Test setup failed - device identifier not set!");
        }

        // Create instance of DeviceParameters for the query
        deviceParameters = new DeviceParameters(testDeviceIdentifier);

        // Run the query from TelemetryConsole
        TelemetryConsole.RunQuery(QueryType.QUERYDEVICE, deviceParameters);
    }

    @AfterAll
    static void tearDown() {
        
    }

    @Test
    void testQueryConstructor() {

        /*
         Precondition:
         -- the user has been authenticated
         -- and deviceDetails contains a valid deviceIdentifier 
        */

        // Check that the user has been authenticated and has sufficient access to run
        // the query
        User authenticatedUser = TelemetryConsole.get_currentUser();
        assertNotNull(authenticatedUser);

        AccessLevel authUserAccess = authenticatedUser.getAccessLevel();
        AccessLevel expectedAccessLvl = testUser.getAccessLevel();
        assertEquals(authUserAccess, expectedAccessLvl);

        // Check that query parameters are not null and contain a valid device identifier
        DeviceParameters currentDevParams = (DeviceParameters)TelemetryConsole.get_currentQuery().getQueryParameters();
        assertNotNull(currentDevParams);
        assertFalse(StringHelper.IsStringNullOrEmpty(currentDevParams.getDeviceIdentifier()));
    }

    @Test
    void testSetQueryResults() {
        /*
        Postcondition:
        -- instance of QueryResults has been linked to self
         */

        // Check that instance of QueryResults has been set
        assertNotNull(TelemetryConsole.get_currentQueryResults());
    }

    @Test
    void testRunQuery() {

        /*
        Postcondition:
        -- creates query based on QueryType of queryDevice and deviceParameters.deviceIdentifier
        -- and creates an instance of DataConnector to query the data source for all corresponding entries
        -- and parses the returned query result data
        -- and creates new instances of Device (QueryItem) for data entry
        -- and creates a new instance of QueryResults
        -- which is linked to the created instances of Device 

         */

        // Check that the correct instance of Query has been created with appropriate implementation
        // of QueryParameters. For an instance of device parameters this should have a parameter count
        // of exactly 1.
        Query createdQuery = TelemetryConsole.get_currentQuery();

        assertEquals(createdQuery.getQueryType(), QueryType.QUERYDEVICE);
        assertEquals(createdQuery.getQueryParameters().ParameterCount, 1);
        assertTrue(createdQuery.getQueryParameters() instanceof DeviceParameters);

        DeviceParameters devParams = (DeviceParameters)createdQuery.getQueryParameters();
        assertEquals(devParams.getDeviceIdentifier(), testDeviceIdentifier);

        // Check that an instance of DataConnector was created and linked to Query
        DataConnector dataConnector = createdQuery.get_dataConnector();
        assertNotNull(dataConnector);

        // Check that the data was parsed from the raw data. To test this we'll use the dataConnector
        // instance from the query to obtain another instance of raw data and call the ParseData method
        // to verify that it was parsed by checking the date string to object translation. Note that this
        // is all done within the RunQuery method in normal operation
        ArrayList<Object[]> testDataRaw = dataConnector.getData(createdQuery.getQueryType(), devParams);
        try {
            ArrayList<QueryItem> parsedData = createdQuery.ParseData(testDataRaw);
            SimpleDateFormat dtFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            for (int i = 0; i < parsedData.size(); i++) {

                Date dataEntryDate = parsedData.get(i).get_dateLogged();
                String parsedDateToString = dtFormat.format(dataEntryDate);
                String expectedDate = (String)testDataRaw.get(i)[0];
                assertEquals(parsedDateToString, expectedDate);
            }
        } catch (Exception e) {
            // If any exception has occurred here then test should be failed. Use assertTrue(false) to do this
            assertTrue(false, "Parse data exception");   
        }

        // We have already tested that a new instance of QueryResults has been created when checking that
        // the QueryResults instance linked to TelemetryConsole was not null, but we need to verify that the
        // concrete implementations of QueryItem are Device and the count in the collection matches the
        // number of entries from the raw data
        List<QueryItem> queryItems = TelemetryConsole.get_currentQueryResults().getQueryItems();
        
        for (QueryItem queryItem : TelemetryConsole.get_currentQueryResults().getQueryItems()) {
            assertTrue(queryItem instanceof Device);
        }

        assertEquals(queryItems.size(), testDataRaw.size());
    }

    @Test
    void testInvalidQuery() {
        // Extension - Invalid query has been entered (i.e. invalid device ID)
        // ToDo ...
        // All serial numbers start with S, so we'll use a different character to ensure that it is invalid
    }
}
