package telemetryconsole.com.example;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import telemetryconsole.com.SampleSetup.Sandbox;
import telemetryconsole.com.SampleSetup.SetupSampleData;
import telemetryconsole.com.example.Common.AccessLevel;
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

        // ToDo - Test preconditions and postconditions where applicable
    }

    @Test
    void testRunQuery() {

        // ToDo - Test any remaining postconditions
    }


}
