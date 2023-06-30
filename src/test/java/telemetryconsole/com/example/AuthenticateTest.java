package telemetryconsole.com.example;

import static org.junit.jupiter.api.Assertions.assertFalse;
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

        // Create a test user - this will be created by the Authenticate operations, but
        // this is to verify tests against.
        testUser = new User(new UserDetails("bFish", "password3"), AccessLevel.USER);
        authenticate = new Authenticate(testUser.getUserDetails());
    }

    @AfterAll
    static void tearDown() {
        
    }

    @Test
    void testAuthenticateConstructor() {

        /* 
        Precondition:
        -- a username and password (UserDetails) have been entered for user
        */

        String username = authenticate.get_userDetails().getUsername();
        String password = authenticate.get_userDetails().getPassword();

        assertFalse(StringHelper.IsStringNullOrEmpty(username));
        assertFalse(StringHelper.IsStringNullOrEmpty(password));
    }

    @Test
    void testAuthenticateUser() {

    }

    @Test
    void testCheckUser() {

    }

    @Test
    void testGet_userDetails() {

    }
}
