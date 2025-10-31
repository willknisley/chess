package dataaccess;

import java.sql.*;
import java.util.Properties;

import static java.sql.DriverManager.getConnection;

public class DatabaseManager {
    private static String databaseName;
    private static String dbUsername;
    private static String dbPassword;
    private static String connectionUrl;
    private static String tableName;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        loadPropertiesFromResources();
    }

    /**
     * Creates the database if it does not already exist.
     */
    static public void createDatabase() throws DataAccessException {
        var statement = "CREATE DATABASE IF NOT EXISTS " + databaseName;
        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
             var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate(statement);
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create database", ex);
        }
    }

    static public void createTables() throws DataAccessException {
        try (var conn = getConnection()) {
             var createUserTable = """
             CREATE TABLE IF NOT EXISTS users (
                     username VARCHAR(255) PRIMARY KEY,
                     password VARCHAR(255) NOT NULL,
                     email VARCHAR(255) NOT NULL
            )
        """;

             var createAuthTable = """
                     CREATE TABLE IF NOT EXISTS auth (
                        authToken VARCHAR(255) PRIMARY KEY,
                        username VARCHAR(255) NOT NULL
             ) 
        """;

             var createGameTable = """
                     CREATE TABLE IF NOT EXISTS game (
                     gameID INT PRIMARY KEY AUTO_INCREMENT,
                     whiteUsername VARCHAR(255),
                     blackUsername VARCHAR(255),
                     gameName VARCHAR(255) NOT NULL,
                     game TEXT
             )
          """;

             try (var statement = conn.createStatement()) {
                 statement.executeUpdate(createUserTable);
                 statement.executeUpdate(createAuthTable);
                 statement.executeUpdate(createGameTable);
             }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create table", ex);
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DatabaseManager.getConnection()) {
     * // execute SQL statements.
     * }
     * </code>
     */
    static Connection getConnection() throws DataAccessException {
        try {
            //do not wrap the following line with a try-with-resources
            var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
            conn.setCatalog(databaseName);
            return conn;
        } catch (SQLException ex) {
            throw new DataAccessException("failed to get connection", ex);
        }
    }

    private static void loadPropertiesFromResources() {
        try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
            if (propStream == null) {
                throw new Exception("Unable to load db.properties");
            }
            Properties props = new Properties();
            props.load(propStream);
            loadProperties(props);
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties", ex);
        }
    }

    private static void loadProperties(Properties props) {
        databaseName = props.getProperty("db.name");
        dbUsername = props.getProperty("db.user");
        dbPassword = props.getProperty("db.password");

        var host = props.getProperty("db.host");
        var port = Integer.parseInt(props.getProperty("db.port"));
        connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
    }
}
