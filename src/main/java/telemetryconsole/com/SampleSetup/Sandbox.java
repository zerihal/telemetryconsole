package telemetryconsole.com.SampleSetup;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import telemetryconsole.com.example.Common.DefaultStrings;

public class Sandbox {

    private static Connection connect(String dbName) {
        // SQLite connection string
        String url = DefaultStrings.SQLiteDBPath + dbName;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
    public static void createNewDatabase(String fileName) {
        
        String url = DefaultStrings.SQLiteDBPath + fileName;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");

                conn.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createNewTable(String dbFileName, String sql) {
        // SQLite connection string
        String url = DefaultStrings.SQLiteDBPath + dbFileName;
        
        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            // create a new table
            System.out.println("Connected to DB - Creating table ...");
            stmt.execute(sql);

            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertUser(String dbName, String username, String password, int accesslevel) {
        String sql = "INSERT INTO users(username,password,accesslevel) VALUES(?,?,?)";

        try (Connection conn = connect(dbName);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setInt(3, accesslevel);
            pstmt.executeUpdate();

            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertDeviceData(String dbName, String dateLogged, String devIdentifier, String deviceName, String deviceTypeName, int status) {
        String sql = "INSERT INTO loggeddata(datelogged,deviceidentifier,devicename,devicetypename,devicestatus) VALUES(?,?,?,?,?)";

        try (Connection conn = connect(dbName);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dateLogged);
            pstmt.setString(2, devIdentifier);
            pstmt.setString(3, deviceName);
            pstmt.setString(4, deviceTypeName);
            pstmt.setInt(5, status);
            pstmt.executeUpdate();

            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void selectAllUsers(String dbName){
        String sql = "SELECT id, username, password, accesslevel FROM users";
        
        try (Connection conn = connect(dbName);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){
            
            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("id") +  "\t" + 
                                   rs.getString("username") + "\t" +
                                   rs.getString("password") + "\t" +
                                   rs.getInt("accesslevel"));
            }

            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void selectAllDevices(String dbName){
        String sql = "SELECT id, datelogged, deviceidentifier, devicename, devicetypename, devicestatus FROM loggeddata";
        
        try (Connection conn = connect(dbName);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){
            
            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("id") +  "\t" + 
                                   rs.getString("datelogged") + "\t" +
                                   rs.getString("deviceidentifier") + "\t" +
                                   rs.getString("devicename") + "\t" +
                                   rs.getString("devicetypename") + "\t" +
                                   rs.getInt("devicestatus"));
            }

            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String selectRandomDeviceIdentifier(String dbName){
        String sql = "SELECT deviceidentifier FROM loggeddata";
        Random rand = new Random();
        int randomRow = rand.nextInt(98) + 1;
        String ident = "";
        
        try (Connection conn = connect(dbName);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){
            
            // loop through the result set
            while (rs.next()) {
                if (rs.getRow() == randomRow) {
                    ident = rs.getString("deviceidentifier");
                    break;
                }
            }

            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return ident;
    }

    public static Boolean dataBaseExists(String dbFilePath) {
        File dbFile = new File(dbFilePath);
        return dbFile.exists() && !dbFile.isDirectory();
    }
}
