package telemetryconsole.com.example.Common;

public final class DefaultStrings {
    private static String _sqliteDbPath;

    private static String get_sqliteDbPath() {
        if (_sqliteDbPath == null) {
            return "jdbc:sqlite:C://sqlite/db/";
        }

        return _sqliteDbPath;
    }

    /**
     * Sets a new folder path for {@link DefaultStrings#SQLiteDBPath}.
     */
    public static void set_sqliteDbPath(String _sqliteDbPath) {
        DefaultStrings._sqliteDbPath = _sqliteDbPath;
    }

    /**
     * Gets the Windows SQLite DB path. 
     */
    public static String WindowsSQLiteDbPath() {
        String sqliteDbPath = get_sqliteDbPath();
        String translatedPath = sqliteDbPath.replace("//", "/").replace('/', '\\').replace("jdbc:sqlite:", "");
        return translatedPath;
    }

    /**
     * Default name to identify an invalid User.
     */
    public static String ErrorUser = "DefaultErrorUser";

    /**
     * Folder path of the SQLite DB. This is C:\sqlite\db by default, however can be
     * set to something different using {@link DefaultStrings#set_sqliteDbPath(String)}.
     */
    public static String SQLiteDBPath = get_sqliteDbPath();

    /**
     * Console users DB name.
     */
    public static String ConsoleUsersDB = "consoleUsers.db";

    /**
     * Device data DB name.
     */
    public static String DeviceDataDB = "deviceData.db";
}
