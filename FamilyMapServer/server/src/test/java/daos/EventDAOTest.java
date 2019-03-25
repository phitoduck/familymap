package daos;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import models.Event;
import models.Person;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by westenm on 2/5/19.
 */

public class EventDAOTest {

    Database db;
    Event bestEvent;
    Event bestEvent2;
    Event bestEvent3;
    ArrayList<Event> events;

    @Before
    public void setUp() throws Exception {
        db = new Database();
        bestEvent = new Event("Biking_123A", "Gale", "Gale123A",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2020);

        bestEvent2 = new Event("Biking_123A123", "Gale", "Gale123A",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2020);

        bestEvent3 = new Event("Biking_123A321", "Gale", "Gale123A",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2020);

        events = new ArrayList<>();
        events.add(bestEvent);
        events.add(bestEvent2);
        events.add(bestEvent3);

        db.createTables();
    }

    @After
    public void tearDown() throws Exception {
        db.clearTables();
    }

    @Test
    public void insertPass() throws Exception {

        Event compareTest = null;
        db.clearTables();
        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);

            eDao.insert(bestEvent);
            compareTest = eDao.find(bestEvent.getEventID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNotNull(compareTest);
        assertEquals(bestEvent, compareTest);

    }

    @Test
    public void insertFail() throws Exception {
        boolean didItWork = true;

        // try to insert the same event twice
        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);
            eDao.insert(bestEvent);
            eDao.insert(bestEvent);
            db.closeConnection(didItWork);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            didItWork = false;
        }
        assertFalse(didItWork);

        // try to find event (shouldn't be there, the db did not commit)
        Event compareTest = bestEvent;
        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);
            compareTest = eDao.find(bestEvent.getEventID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNull(compareTest);

    }

    @Test
    public void findPass() throws Exception {

        Event foundEvent = null;

        // insert an event into the table
        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);
            eDao.insert(bestEvent);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        // try to find it
        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);
            foundEvent = eDao.find(bestEvent.getEventID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        Assert.assertEquals(bestEvent, foundEvent);

    }

    @Test
    public void findFail() throws Exception {

        Event foundEvent = null;
        String fakeID = "i'm fake!";

        // try to find a nonexistent event
        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);
            foundEvent = eDao.find(fakeID);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        // event should be null
        Assert.assertNull(foundEvent);

    }

    @Test
    public void deletePass() throws DataAccessException {

        Event foundEvent = null;

        try {
            // insert a person into the database
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);
            eDao.insert(bestEvent);

            // retrieve the person
            foundEvent = eDao.find(bestEvent.getEventID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        // verify the person is in the database
        Assert.assertEquals(bestEvent, foundEvent);

        try {
            // delete the entire people table
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);
            eDao.delete();

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        // attempt to retrieve the person (should return null)
        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);
            foundEvent = eDao.find(bestEvent.getEventID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        Assert.assertNull(foundEvent);

    }

    @Test(expected = daos.DataAccessException.class)
    public void deleteFail() throws DataAccessException {

        try {

            // delete the table from the database
            Connection conn = db.openConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("drop table if exists " + Database.EVENTS);
            db.closeConnection(true);

            // try to delete the non-existent table
            conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);
            eDao.delete();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DataAccessException e) {
            throw new DataAccessException("FAILED!");
        } finally {
            // put the tables back for tear down function!
            db.createTables();
        }



    }

    @Test
    public void insertManyPASS() throws Exception {


        Event compareTest = null;
        db.clearTables();
        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);

            // insert many
            eDao.insertMany(events);

            // try to find one
            compareTest = eDao.find(bestEvent.getEventID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNotNull(compareTest);
        assertEquals(bestEvent, compareTest);
    }

    @Test()
    public void insertManyFAIL() throws Exception {

        boolean didItWork = true;

        // try to insert the same event twice
        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);

            eDao.insertMany(events);
            eDao.insertMany(events);
            db.closeConnection(didItWork);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            didItWork = false;
        }
        assertFalse(didItWork);

        // try to find event (shouldn't be there, the db did not commit)
        Event compareTest = bestEvent;
        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);
            compareTest = eDao.find(bestEvent.getEventID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNull(compareTest);


    }

}