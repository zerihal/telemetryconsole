package telemetryconsole.com.example.Common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UserDBConnector {

    public Connection connect() {
        // SQLite connection string
        String url = DefaultStrings.SQLiteDBPath + DefaultStrings.ConsoleUsersDB;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    // Override here is just to test reflection
    @Override 
    public String toString() {
        return "UserDBConnector";
    }
}
