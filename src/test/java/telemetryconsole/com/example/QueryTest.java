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

import telemetryconsole.com.SampleSetup.ISetupSample;
import telemetryconsole.com.SampleSetup.Sandbox;
import telemetryconsole.com.SampleSetup.SetupSampleData;
import telemetryconsole.com.SampleSetup.SetupSampleUsers;
import telemetryconsole.com.example.Common.AccessLevel;
import telemetryconsole.com.example.Common.DataConnector;
import telemetryconsole.com.example.Common.DefaultStrings;
import telemetryconsole.com.example.Common.DeviceParameters;
import telemetryconsole.com.example.Common.ParseDataException;
import telemetryconsole.com.example.Common.QueryType;
import telemetryconsole.com.example.Common.QueryValidator;
import telemetryconsole.com.example.Common.User;
import telemetryconsole.com.example.Common.UserDetails;
import telemetryconsole.com.example.TestHelpers.TestHelper;
import telemetryconsole.com.example.Util.StringHelper;

public class QueryTest {

    // Fixture declarations 
    private static User testUser;
    private static DeviceParameters deviceParameters;
    private static String testDeviceIdentifier;
    private static User currentUser;
    private static QueryType currentQueryType;

    @BeforeAll
    public static void setUp() throws Exception {

        // Setup sample device data DB
        ISetupSample setupSampleData = new SetupSampleData();
        setupSampleData.RunSetup();

        // Sample user DB may already exist from AuthenticateTest setup (if that was run first) - check this and
        // if not then create it as also required for this test set
        if (!Sandbox.DataBaseExists(DefaultStrings.WindowsSQLiteDbPath() + DefaultStrings.ConsoleUsersDB)) {
            System.out.println("Sample users DB required for this test set but does not exist - recreating ...");
            setupSampleData = new SetupSampleUsers();
            setupSampleData.RunSetup();
        } else {
            System.out.println("Sample users DB found - no recreation required");
        }

        // Create an authenticated test user to check preconditions
        testUser = new User(new UserDetails("bFish", "password3"), AccessLevel.USER);
        currentUser = new Authenticate(testUser.get_userDetails().getUsername(), testUser.get_userDetails().getPassword()).DoAuthentication();

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

    // TC4 - Main success scenario for runQuery
    @Test
    void testRunQuery() {

        /*
         Precondition:
         -- the user has been authenticated        
        */

        assertNotNull(currentUser);

        AccessLevel authUserAccess = currentUser.get_accessLevel();
        AccessLevel expectedAccessLvl = testUser.get_accessLevel();
        assertEquals(authUserAccess, expectedAccessLvl);

        /*
         -- and deviceDetails contains a valid deviceIdentifier 
        */
        
        assertNotNull(deviceParameters);;
        assertTrue(QueryValidator.IsValidSerialNo(deviceParameters.getDeviceIdentifier()));

        /*
        Postcondition:
        -- creates query based on QueryType and DeviceParameters
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
        -- and executes query
        -- which creates a new instance of DataConnector
        */

        QueryResults queryResults = null;

        try {
            queryResults = query.ExecuteQuery();
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
        -- and uses the DataConnector to obtain raw query data
        -- and parses the returned data
        */

        // To test this we'll use the dataConnector instance from the query to obtain another instance of 
        // raw data and call the ParseData* method to verify that it was parsed by checking the date string 
        // to object translation. 
        // * ParseData is an internal method in Query, so we get this from TestHelper which uses reflection or 
        // a copy for testing purposes.
        
        ArrayList<Object[]> testDataRaw = dataConnector.GetData(query.getQueryType(), devParams, null);

        try {
            ArrayList<QueryItem> parsedData = TestHelper.ParseDeviceDataInternal(query, testDataRaw);
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
        -- and creates new instances of Device (QueryItem) for data entries
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
        assertTrue(queryItems.size() > 0);

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
    
    // TC5 - Precondition not satisfied - unauthorised user
    @Test
    void testUnauthenticatedUser() {

        // This precondition is checked in the system class rather than by the runQuery operation, and would
        // normally relate to a UI option to run a query being disabled if failed. In order to test this at
        // this point, we need to get the private method from TelemetryConsole for CanRunQuery and set an 
        // unauthorised user.

        TelemetryConsole telemetryConsole = new TelemetryConsole();
        UserDetails badUser = new UserDetails("iFake", "password123");
        telemetryConsole.set_currentUser(new User(badUser, AccessLevel.NONE));
        DeviceParameters testDevPars = new DeviceParameters(testDeviceIdentifier);

        boolean canQuery = TestHelper.CanRunQueryInternal(telemetryConsole, QueryType.QUERYDEVICE, testDevPars);

        assertFalse(canQuery);
    }

    // TC6 - Precondition not satisfied - invalid query parameters (device identifier)
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

    // TC7 - Postcondition not satisfied - no results found for device
    @Test
    void testNoResults() {

        // When the sample data DB is setup, device identifers are randomly generated starting with S (to be 
        // valid), but are all 10 alphanumeric characters long, so to ensure one that will never be in the
        // DB, we create one with 11 characters for this test
        String noEntriesDeviceIdent = "S123456789E";
        DeviceParameters devPars = new DeviceParameters(noEntriesDeviceIdent);

        Query query = new Query(currentQueryType, devPars);

        QueryResults queryResults = null;

        try {
            queryResults = query.ExecuteQuery();
        } catch (ParseDataException e) {
            // Use a simple assert null check on the exception to fail the test if thrown
            assertNull(e, "Unexpected parse data exception");
        }

        assertTrue(queryResults.getQueryItems().size() == 0);
    }
}
