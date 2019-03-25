package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import models.Event;
import models.Model;

/**
 * Manages insertion and access of Events in a database.
 *
 * Created by eric on 2/13/19.
 */

public class EventDAO extends DAO {

    public EventDAO(Connection connection) {
        super(connection);
    }

    /**
     * @param event Event to be inserted as a row into the database.
     * @return true if no errors were thrown while attempting to insert the event.
     * @throws DataAccessException if the insertion fails at any point.
     * */
    public boolean insert(Event event) throws DataAccessException {
        boolean commit = true;
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO " + Database.EVENTS + " (event_id, userName, person_id, latitude, longitude, " +
                "country, city, event_type, year) VALUES(?,?,?,?,?,?,?,?,?)";

        try {

            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getUserName());
            stmt.setString(3, event.getPersonID());
            stmt.setDouble(4, event.getLatitude());
            stmt.setDouble(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());

            stmt.executeUpdate();

        } catch (SQLException e) {
            commit = false;
            throw new DataAccessException("Error encountered while inserting into the database");
        }

        return commit;
    }

    /**
     *
     * Inserts many events into the database
     *
     * @param events
     * @return
     * @throws DataAccessException
     */
    public boolean insertMany(Collection<Event> events) throws DataAccessException {

        for (Event event : events) {
            insert(event);
        }

        return true;

    }

    /**
     *
     * @param eventID ID Number of the event to lookup in its respective table in the database.
     * @return Event in the Events table with the specified ID, or null if Event is not found
     */
    @Override
    public Event find(String eventID) throws DataAccessException {
        Event event = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM " + Database.EVENTS + " WHERE event_id = ?;";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (rs.next() == true) {
                event = new Event(
                        rs.getString("event_id"),
                        rs.getString("userName"),
                        rs.getString("person_id"),
                        rs.getFloat("latitude"),
                        rs.getFloat("longitude"),
                        rs.getString("country"),
                        rs.getString("city"),
                        rs.getString("event_type"),
                        rs.getInt("year"));
                return event;
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        }
        return null;
    }

    /**
     *
     * Find ALL of a user's events
     *
     * @param userName
     * @return ArrayList of all events associated with the given user
     */
    public ArrayList<Event> findAll(String userName) throws DataAccessException {

        ArrayList<Event> toReturn = new ArrayList<>();

        Event event = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM " + Database.EVENTS + " WHERE userName = ?;";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userName);
            rs = stmt.executeQuery();
            while (rs.next()) {
                event = new Event(
                        rs.getString("event_id"),
                        rs.getString("userName"),
                        rs.getString("person_id"),
                        rs.getFloat("latitude"),
                        rs.getFloat("longitude"),
                        rs.getString("country"),
                        rs.getString("city"),
                        rs.getString("event_type"),
                        rs.getInt("year"));
                toReturn.add(event);
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        }

        return toReturn;
    }

    @Override
    public void delete() throws DataAccessException {

        // drop all entries from each table
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("delete from " + Database.EVENTS);
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
