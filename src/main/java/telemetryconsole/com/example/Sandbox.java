package telemetryconsole.com.example;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Sandbox {

    private static Connection Connect(String dbName) {
        // SQLite connection string
        String url = "jdbc:sqlite:C://sqlite/db/" + dbName;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
    public static void CreateNewDatabase(String fileName) {

        String url = "jdbc:sqlite:C:/sqlite/db/" + fileName;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void CreateNewUsersTable(String dbFileName) {
        // SQLite connection string
        String url = "jdbc:sqlite:C://sqlite/db/" + dbFileName;
        
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS users (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	username text NOT NULL,\n"
                + "	password text NOT NULL,\n"
                + " accesslevel integer NOT NULL\n"
                + ");";
        
        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            // create a new table
            System.out.println("Connected to DB - Creating table ...");
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void InsertUser(String dbName, String username, String password, int accesslevel) {
        String sql = "INSERT INTO users(username,password,accesslevel) VALUES(?,?,?)";

        try (Connection conn = Connect(dbName);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setInt(3, accesslevel);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void SelectAllUsers(String dbName){
        String sql = "SELECT id, username, password, accesslevel FROM users";
        
        try (Connection conn = Connect(dbName);
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("id") +  "\t" + 
                                   rs.getString("username") + "\t" +
                                   rs.getString("password") + "\t" +
                                   rs.getInt("accesslevel"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
