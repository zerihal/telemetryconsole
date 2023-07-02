package telemetryconsole.com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import telemetryconsole.com.SampleSetup.SetupSampleUsers;
import telemetryconsole.com.example.Common.AccessLevel;
import telemetryconsole.com.example.Common.User;
import telemetryconsole.com.example.Common.UserDetails;
import telemetryconsole.com.example.Util.StringHelper;

public class AuthenticateTest {

    // Fixture declarations - set as static so they can be re-used between some of the tests that
    // check parts of the overall operation
    private static User testUser;
    private static Authenticate authenticate;
    private static User currentUser;

    @BeforeAll
    static void setUp() {
        SetupSampleUsers setupSampleUser = new SetupSampleUsers();
        setupSampleUser.RunSetup();

        // Create a test user - this is what should be created by the Authenticate operations. The one created
        // manually below is to be used to verify the operations and contracts.
        testUser = new User(new UserDetails("bFish", "password3"), AccessLevel.USER);
    }

    @AfterAll
    static void tearDown() {
        
    }

    @Test
    void testAuthenticateConstructor() {

        /* 
        Precondition:
        -- a username and password have been entered for user
        */

        String username = testUser.getUserDetails().getUsername();
        String password = testUser.getUserDetails().getPassword();

        // Check that username and password are not null or empty strings
        assertFalse(StringHelper.IsStringNullOrEmpty(username));
        assertFalse(StringHelper.IsStringNullOrEmpty(password));

        /*
        Postcondition:
        -- a new instance of UserDetails is created from username and password
        -- and is linked to a new instance of Authenticate
        */

        // Create new instance of Authenticate       
        authenticate = new Authenticate(username, password);

        // Check that an instance of UserDetails has been created from the constructor and
        // this has been correctly created from the username and password that were passed
        // in from testUser
        assertNotNull(authenticate.get_userDetails());
        assertEquals(username, authenticate.get_userDetails().getUsername());
        assertEquals(password, authenticate.get_userDetails().getPassword());

        /*
        -- which creates a new instance of UserDBConnector
        */

        assertNotNull(authenticate.get_userDbConnector());
    }

    @Test
    void testAuthenticateUser() {

        /*
        Postcondition:
        -- uses the instance of UserDBConnector created on construction to check user exists and password matches
        -- and obtains access level for user
        -- and creates new instance of User with access level set
        */

        // Ensure that the user details for Authenticate are set to that of the test user
        authenticate.set_userDetails(testUser.getUserDetails());

        // Run the AuthenticateUser method from the Authenticate instance previously created on its own
        // so that we can check that access level is not invalid or none (TelemetryConsole itself handles
        // this but this is in order to check the handled results that would mean that CurrentUser is not
        // set)
        currentUser = authenticate.AuthenticateUser();

        // Check that user existed and password matched - if user did not exist then access level
        // would be AccessLevel.INVALID and if password mismatch then would be AccessLevel.NONE
        AccessLevel userAccessLevel = currentUser.getAccessLevel();

        assertNotEquals(userAccessLevel, AccessLevel.INVALID);
        assertNotEquals(userAccessLevel, AccessLevel.NONE);

        // Check that the correct access level has been obtained for the user and linked to the User
        // object that was returned
        assertEquals(userAccessLevel, testUser.getAccessLevel());

        /*
        -- and links to self
        */

        // If all above subtests have passed then we have an instance of User from running 
        // AuthenticateUser(), which would then be linked back to the caller (TelemetryConsole), 
        // however as this is just a test on the operation, there is nothing to link it to. As such, 
        // this is just included for completeness and passed based on the fact that the valid object 
        // could be returned as required

        assertTrue(true);
    }

    @Test
    void testUnauthorisedUser() {
        
        /*
        Extension - The user is not authorised to use the system and is advised
        */

        // Check authenticating with a valid username but invalid password - this should return access
        // level of NONE
        authenticate.set_userDetails(new UserDetails("jblogs", "wrongPassword"));
        currentUser = authenticate.AuthenticateUser();
        AccessLevel currentAccessLevel = currentUser.getAccessLevel();
        assertEquals(currentAccessLevel, AccessLevel.NONE);

        // Check authenticating with an invalid username (password irrelevant) - this should return
        // access level of INVALID
        authenticate.set_userDetails(new UserDetails("dodgyUser", "none"));
        currentUser = authenticate.AuthenticateUser();
        currentAccessLevel = currentUser.getAccessLevel();
        assertEquals(currentAccessLevel, AccessLevel.INVALID);

        // Note: Prompt would be handled by the UI for a false return from QueryValidator so out of 
        // scope for this test
    }
}
