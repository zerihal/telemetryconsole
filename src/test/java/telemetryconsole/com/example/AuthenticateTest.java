package telemetryconsole.com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import telemetryconsole.com.SampleSetup.SetupSampleUsers;
import telemetryconsole.com.example.Common.AccessLevel;
import telemetryconsole.com.example.Common.User;
import telemetryconsole.com.example.Common.UserDetails;
import telemetryconsole.com.example.Util.StringHelper;

public class AuthenticateTest {

    static Authenticate authenticate;
    static User testUser;

    @BeforeAll
    static void setupSampleDB() {
        SetupSampleUsers setupSampleUser = new SetupSampleUsers();
        setupSampleUser.RunSetup();

        // Create a test user - this is what should be created by the Authenticate operations. The one created
        // manually below is to be used to verify the operations and contracts.
        testUser = new User(new UserDetails("bFish", "password3"), AccessLevel.USER);
        TelemetryConsole.AuthenticateUser(testUser.getUserDetails().getUsername(), testUser.getUserDetails().getPassword());
        authenticate = TelemetryConsole.get_authenticate();
    }

    @AfterAll
    static void tearDown() {
        
    }

    @Test
    void testAuthenticateConstructor() {

        /* 
        Precondition:
        -- a username and password have been entered for user

        Postcondition:
        -- a new instance of UserDetails is created from username and password
        -- and is linked to a new instance of Authenticate
        -- which creates a new instance of UserDBConnector
        */

        String username = testUser.getUserDetails().getUsername();
        String password = testUser.getUserDetails().getPassword();

        // Check that username and password are not null or empty strings
        assertFalse(StringHelper.IsStringNullOrEmpty(username));
        assertFalse(StringHelper.IsStringNullOrEmpty(password));

        // Check that an instance of UserDetails has been created from the constructor and
        // this has been correctly created from the username and password that were passed
        // in from testUser
        assertNotNull(authenticate.get_userDetails());
        assertEquals(username, authenticate.get_userDetails().getUsername());
        assertEquals(password, authenticate.get_userDetails().getPassword());

        // Check that UserDBConnector has been created
        assertNotNull(authenticate.get_userDbConnector());
    }

    @Test
    void testAuthenticateUser() {

        /*
        Postcondition:
        -- uses the instance of UserDBConnector created on construction to check user exists and password matches
        -- and obtains access level for user
        -- and creates new instance of User with access level set
        -- and links to TelemetryConsole
        */

        // Run the AuthenticateUser method from the Authenticate instance in TelemetryConsole on its own
        // so that we can check that access level is not invalid or none (TelemetryConsole itself handles
        // this but this is in order to check the handled results that would mean that CurrentUser is not
        // set)
        User authenticatedUser = authenticate.AuthenticateUser();

        // Check that user existed and password matched - if user did not exist then access level
        // would be AccessLevel.INVALID and if password mismatch then would be AccessLevel.NONE
        AccessLevel userAccessLevel = authenticatedUser.getAccessLevel();

        assertNotEquals(userAccessLevel, AccessLevel.INVALID);
        assertNotEquals(userAccessLevel, AccessLevel.NONE);

        // Check that the correct access level has been obtained for the user and linked to the User
        // object that was returned
        assertEquals(userAccessLevel, testUser.getAccessLevel());

        // Check that the user authenticated user was linked to TelemetryConsole
        assertNotNull(TelemetryConsole.getCurrentUser());
    }
}
