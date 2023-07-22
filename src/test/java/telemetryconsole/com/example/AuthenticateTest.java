package telemetryconsole.com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import telemetryconsole.com.SampleSetup.SetupSampleUsers;
import telemetryconsole.com.example.Common.AccessLevel;
import telemetryconsole.com.example.Common.InvalidUserDetailsException;
import telemetryconsole.com.example.Common.User;
import telemetryconsole.com.example.Common.UserDBConnector;
import telemetryconsole.com.example.Common.UserDetails;

public class AuthenticateTest {

    // Fixture declarations
    private static User testUser;

    @BeforeAll
    static void setUp() {
        SetupSampleUsers setupSampleUser = new SetupSampleUsers();
        setupSampleUser.RunSetup();

        // Create a test user - this is what should be created by the Authenticate operations. The one created
        // manually below is to be used to verify the operations and contracts, however this user is one that
        // is present in the users DB that was created by the setup methods above so can be used to test the
        // authentication method to verify that correct access level is returned from the username and password.
        testUser = new User(new UserDetails("bFish", "password3"), AccessLevel.USER);
    }

    @AfterAll
    static void tearDown() {
        
    }

    // TC1 - Main success scenario for authenticateUser
    @Test
    void testAuthenticateUser() {

        /* 
        Precondition:
        -- a username and password have been entered for user
        */

        // Create new instance of Authenticate for the test user
        String username = testUser.get_userDetails().getUsername();
        String password = testUser.get_userDetails().getPassword();

        Authenticate authenticate = null;
        InvalidUserDetailsException userEx = null;
        
        try {
            authenticate = new Authenticate(username, password);
        } catch (InvalidUserDetailsException e) {
            userEx = e;
        }

        // Check that invalid user details exception was not thrown (i.e. username
        // and password were not null or empty strings)
        assertNull(userEx);

        /*
        Postcondition:
        -- a new instance of UserDetails is created from username and password
        -- and is linked to a new instance of Authenticate
        */

        // Check UserDetails instance is set in the new Authenticate instance (i.e.
        // should not be null)
        assertNotNull(authenticate.get_userDetails());        

        /*
        -- which creates a new instance of UserDBConnector
        */

        // The UserDBConnector instance is encapsulated in the Authenticate instance as only
        // really required internally, so for this test use reflection to get the property

        UserDBConnector authUserDBConnector = null;

        try {
            Field userDBConnectorField = Authenticate.class.getDeclaredField("_userDbConnector");
            userDBConnectorField.setAccessible(true);
            authUserDBConnector = (UserDBConnector)userDBConnectorField.get(authenticate);
        } catch (Exception e) {
            System.out.println("Reflection exception getting private field - " + e.getMessage());
            e.printStackTrace();
        }

        assertNotNull(authUserDBConnector);

        /*
        Postcondition:
        -- uses the instance of UserDBConnector created on construction to check user exists and password matches
        -- and obtains access level for user
        -- and creates new instance of User with access level set
        */

        // Run the AuthenticateUser method from the Authenticate instance previously created on its own
        // so that we can check that access level is not invalid or none (TelemetryConsole itself handles
        // this but this is in order to check the handled results that would mean that CurrentUser is not
        // set)
        User currentUser = authenticate.DoAuthentication();

        // Check that user existed and password matched - if user did not exist then access level
        // would be AccessLevel.INVALID and if password mismatch then would be AccessLevel.NONE
        AccessLevel userAccessLevel = currentUser.get_accessLevel();

        assertNotEquals(userAccessLevel, AccessLevel.INVALID);
        assertNotEquals(userAccessLevel, AccessLevel.NONE);

        // Check that the correct access level has been obtained for the user and linked to the User
        // object that was returned
        assertEquals(userAccessLevel, testUser.get_accessLevel());

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

    // TC2 - Precondition not satisfied - Missing username or password
    @Test
    void testInvalidUserEntry() {

        /*
         * Check that passing a null username and empty password (could be any combination of null, empty or 
         * white space in either though) that an InvalidUserDetailsException is thrown when trying to create
         * an instance of Authenticate. In reality this might be checked beforehand in the UI with a "login"
         * button only enabled as appropriate, but may still be useful to throw for testing purposes.
         */

        String username = null;
        String password = "";

        InvalidUserDetailsException e = assertThrows(InvalidUserDetailsException.class, () -> {
            new Authenticate(username, password);
        });

        String exMessage = e.getMessage();
        System.out.println("Invalid user details exception message (expected):\r\n" + exMessage);

        assertTrue(exMessage.contains("Invalid user details"));
    }

    // TC3 - Postcondition not satisfied - user does not exist or incorrect password
    @Test
    void testUnauthorisedUser() throws InvalidUserDetailsException {
        
        /*
        Extension - The user is not authorised to use the system and is advised
        */

        // Note: For both the below we are entering something for username and password so no need to 
        // handle any potential InvalidUserDetailsException - just added throws from method declaration 
        // to allow to compile

        // Check authenticating with a valid username but invalid password - this should return access
        // level of NONE
        Authenticate authenticate = new Authenticate("jblogs", "wrongassword");
        User currentUser = authenticate.DoAuthentication();
        AccessLevel currentAccessLevel = currentUser.get_accessLevel();
        assertEquals(currentAccessLevel, AccessLevel.NONE);

        // Check authenticating with an invalid username (password irrelevant) - this should return
        // access level of INVALID
        authenticate.set_userDetails(new UserDetails("dodgyUser", "none"));
        currentUser = authenticate.DoAuthentication();
        currentAccessLevel = currentUser.get_accessLevel();
        assertEquals(currentAccessLevel, AccessLevel.INVALID);

        // Note: Prompt would be handled by the UI for a false return from QueryValidator so out of 
        // scope for this test
    }
}
