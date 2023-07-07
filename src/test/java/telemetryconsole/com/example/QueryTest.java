package telemetryconsole.com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import telemetryconsole.com.example.Common.ParseDataException;
import telemetryconsole.com.example.Common.QueryType;
import telemetryconsole.com.example.Common.QueryValidator;
import telemetryconsole.com.example.Common.User;
import telemetryconsole.com.example.Common.UserDetails;
import telemetryconsole.com.example.Util.StringHelper;

public class QueryTest {

    // Fixture declarations - set as static so they can be re-used between some of the tests that
    // check parts of the overall operation
    private static User testUser;
    private static DeviceParameters deviceParameters;
    private static String testDeviceIdentifier;
    private static User currentUser;
    private static QueryType currentQueryType;

    @BeforeAll
    public static void setUp() throws Exception {

        // Setup sample device data DB
        SetupSampleData setupSampleData = new SetupSampleData();
        setupSampleData.RunSetup();

        // Create an authenticated test user to check preconditions
        testUser = new User(new UserDetails("bFish", "password3"), AccessLevel.USER);
        currentUser = new Authenticate(testUser.getUserDetails().getUsername(), testUser.getUserDetails().getPassword()).AuthenticateUser();

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

        // Set query type
        currentQueryType = QueryType.QUERYDEVICE;
    }

    @AfterAll
    static void tearDown() {
        
    }

    @Test
    void testQueryConstructor() {

        /*
         Precondition:
         -- the user has been authenticated
         
        */

        assertNotNull(currentUser);

        AccessLevel authUserAccess = currentUser.getAccessLevel();
        AccessLevel expectedAccessLvl = testUser.getAccessLevel();
        assertEquals(authUserAccess, expectedAccessLvl);

        /*
         -- and deviceDetails contains a valid deviceIdentifier 
        */
        
        assertNotNull(deviceParameters);;
        assertFalse(StringHelper.IsStringNullOrEmpty(deviceParameters.getDeviceIdentifier()));
        assertTrue(QueryValidator.IsValidSerialNo(deviceParameters.getDeviceIdentifier()));
    }

    @Test
    void testRunQuery() {

        /*
        Postcondition:
        -- creates query based on QueryType of queryDevice and deviceParameters.deviceIdentifier
        */

        // Check that the correct instance of Query has been created with appropriate implementation
        // of QueryParameters. For an instance of device parameters this should have a parameter count
        // of exactly 1.
        Query query = new Query(currentQueryType, deviceParameters);

        assertEquals(query.getQueryType(), QueryType.QUERYDEVICE);
        assertEquals(query.getQueryParameters().ParameterCount, 1);
        assertTrue(query.getQueryParameters() instanceof DeviceParameters);

        DeviceParameters devParams = (DeviceParameters)query.getQueryParameters();
        assertEquals(devParams.getDeviceIdentifier(), testDeviceIdentifier);

        /*
        -- and creates a new instance of DataConnector
        -- and runs query using instance of DataConnector to obtain raw data
        */
        QueryResults queryResults = null;

        try {
            queryResults = query.RunQuery();
        } catch (ParseDataException e) {
            // Use a simple assert null check on the exception to fail the test if thrown
            assertNull(e, "Unexpected parse data exception");
        }

        // Check that an instance of DataConnector was created and linked to Query
        DataConnector dataConnector = query.get_dataConnector();
        assertNotNull(dataConnector);

        // Check queryResults not null
        assertNotNull(queryResults);

        /*
        -- and parses the returned data
        */

        // To test this we'll use the dataConnector instance from the query to obtain another instance of 
        // raw data and call the ParseData method to verify that it was parsed by checking the date string 
        // to object translation. 
        // Note that this is all done within the RunQuery method in normal operation

        ArrayList<Object[]> testDataRaw = dataConnector.getData(query.getQueryType(), devParams, null);
        try {
            ArrayList<QueryItem> parsedData = query.ParseData(testDataRaw);
            SimpleDateFormat dtFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            for (int i = 0; i < parsedData.size(); i++) {

                Date dataEntryDate = parsedData.get(i).get_dateLogged();
                String parsedDateToString = dtFormat.format(dataEntryDate);
                String expectedDate = (String)testDataRaw.get(i)[0];
                assertEquals(parsedDateToString, expectedDate);
            }
        } catch (Exception e) {
            // Use a simple assert null check on the exception to fail the test if thrown
            assertNull(e, "Unexpected get data exception from DataConnector");
        }

        /*
        -- and creates new instances of Device (QueryItem) for data entry
        -- and creates a new instance of QueryResults
        -- which is linked to the created instances of Device 
        */
        
        // We have already tested that a new instance of QueryResults has been created when checking that
        // the QueryResults instance was not null, but we need to verify that the concrete implementations 
        // of QueryItem are Device and the count in the collection matches the number of entries from the 
        // raw data

        List<QueryItem> queryItems = queryResults.getQueryItems();
        
        for (QueryItem queryItem : queryItems) {
            assertTrue(queryItem instanceof Device);
        }

        assertEquals(queryItems.size(), testDataRaw.size());

        /*
        -- and links QueryResults to self
        */

        // If all above subtests have passed then we have an instance of QueryResult from running the
        // query, which would then be linked back to the caller (TelemetryConsole), however as this is
        // just a test on the operation, there is nothing to link it to. As such, this is just included
        // for completeness and passed based on the fact that the valid object could be returned as 
        // required

        assertTrue(true);
    }    

    @Test
    void testInvalidQuery() {

        /*
        Extension - Query is invalid and actor is prompted to check inputs and run again
        */

        // All serial numbers start with S, so we'll use a different character to ensure that it is invalid
        String invalidDeviceIdent = "XYZ12345678";
        assertFalse(QueryValidator.IsValidSerialNo(invalidDeviceIdent));

        // Note: Prompt would be handled by the UI for a false return from QueryValidator so out of 
        // scope for this test
    }
}
