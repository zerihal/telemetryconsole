package telemetryconsole.com.example.Util;

public final class StringHelper {
    
    /**
     * Checks whether a string is null, empty or white space.
     */
    public static boolean isStringNullOrEmpty(String string) {
        if (string == null || string.isEmpty() || string.trim().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

}
