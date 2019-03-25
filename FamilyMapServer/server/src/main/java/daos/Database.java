package daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import daos.DataAccessException;

/**
 * Created by westenm on 2/4/19.
 */

public class Database {

    // table names
    public static final String PEOPLE = "people";
    public static final String EVENTS = "events";
    public static final String USERS = "users";
    public static final String AUTH_TOKENS = "auth_tokens";

    // database path
    private static final String FMS_DATABASE_PATH = "server/src/main/resources/fms.db";

    private Connection conn;

    static {
        try {
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection openConnection() throws DataAccessException {
        try {

            final String CONNECTION_URL = "jdbc:sqlite:" + FMS_DATABASE_PATH;

            // Open a database connection to the file given in the path
            conn = DriverManager.getConnection(CONNECTION_URL);

            // Start a transaction
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to open connection to database");
        }

        return conn;
    }

    public void closeConnection(boolean commit) throws DataAccessException {
        try {
            if (commit) {
                conn.commit();
            } else {
                conn.rollback();
            }

            conn.close();
            conn = null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to close database connection");
        }
    }

    public void createTables() throws DataAccessException {

        openConnection();

        try {

            /*
                create people table
             */
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("create table if not exists " + PEOPLE + " (\n" +
                    "   person_id   varchar(255) not null primary key,            -- Unique identifier for this person (non-empty string)\n" +
                    "   descendant  varchar(255) not null,                      -- User (UserName) to which this person belongs\n" +
                    "   first_name  varchar(255) not null,                        -- Person's first name (non-empty string)\n" +
                    "   last_name   varchar(255) not null,                        -- Person's last name (non-empty string)\n" +
                    "   gender      char(1) not null check (gender in (\"m\", \"f\")),-- Person's gender (string: 'm' or 'f')\n" +
                    "   father_id   varchar(255),                                 -- ID of person's father (possibly null)\n" +
                    "   mother_id   varchar(255),                                 -- ID of person's mother (possibly null)\n" +
                    "   spouse_id   varchar(255)                                  -- ID of person's spouse (possibly null)\n" +
                    ");");

            /*
                create users table
            */
            stmt.executeUpdate("create table if not exists " + USERS + " (" +
                    "    userName    varchar(255) not null primary key,           -- Unique user name (non-empty string)\n" +
                    "    password    varchar(255) not null,                       -- password (non-empty string)\n" +
                    "    email       varchar(255) not null,                       -- email address (non-empty string)\n" +
                    "    first_name  varchar(255) not null,                       -- first name (non-empty string)\n" +
                    "    last_name   varchar(255) not null,                       -- last name (non-empty string)\n" +
                    "    gender      char(1) check (gender in (\"m\", \"f\")),    -- gender (string: 'm' or 'f')\n" +
                    "    person_id   varchar(255) not null                        -- Unique Person ID assigned to this user's generated Person object\n" +
                    ");");

            /*
                create events table
            */
            stmt.executeUpdate("create table if not exists " + EVENTS + " (" +
                    "    event_id    varchar(255) not null primary key,            -- Unique identifier for this event (non-empty string)\n" +
                    "    userName    varchar(255) not null,                        -- User to which this person belongs\n" +
                    "    person_id   varchar(255) not null,                             -- ID of person to which this event belongs\n" +
                    "    latitude    real not null,                                -- Latitude of event's location\n" +
                    "    longitude   real not null,                                -- Longitude of event's location\n" +
                    "    country     varchar(255) not null,                        -- Country in which event occurred\n" +
                    "    city        varchar(255) not null,                        -- City in which event occurred\n" +
                    "    event_type  varchar(255) not null,                        -- Type of event (birth, baptism, christening, marriage, death, etc.)\n" +
                    "    year        integer not null                              -- Year in which event occurred\n" +
                    ");");

            /*
                create auth tokens table
            */
//            stmt.executeUpdate("create table if not exists " + AUTH_TOKENS);
            stmt.executeUpdate("create table if not exists " + AUTH_TOKENS + " (" +
                    "   token       varchar(255) not null primary key,           -- Unique ID number of login session\n" +
                    "   userName    varchar(255) not null,                       -- id of user associated with login session\n" +
                    "       foreign key(userName) references " + USERS + "(userName)" +
                    ");");

            closeConnection(true);

        } catch (DataAccessException e) {
            closeConnection(false);
            throw e;
        } catch (SQLException e) {
            closeConnection(false);
            throw new DataAccessException("SQL Error encountered while creating tables");
        }


    }

    public void clearTables() throws DataAccessException
    {
        openConnection();

        // drop all entries from each table
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("delete from " + Database.EVENTS);
            stmt.executeUpdate("delete from " + Database.PEOPLE);
            stmt.executeUpdate("delete from " + Database.USERS);
            stmt.executeUpdate("delete from " + Database.AUTH_TOKENS);
            closeConnection(true);
        } catch (DataAccessException e) {
            closeConnection(false);
            throw e;
        } catch (SQLException e) {
            closeConnection(false);
            throw new DataAccessException(e.getMessage());
        }

    }
}
