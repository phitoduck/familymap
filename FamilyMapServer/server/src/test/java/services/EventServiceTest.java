package services;

import com.google.gson.Gson;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import daos.DataAccessException;
import daos.Database;
import daos.EventDAO;
import models.Event;
import models.Person;
import responses.EventMultipleResponse;

/**
 * Created by eric on 2/23/19.
 */
public class EventServiceTest {

    private Database db;
    private Person person;
    private Event event1;
    private Event event2;
    private Event event3;

    @Before
    public void setUp() throws Exception {
        db = new Database();
        person = new Person(
                "10000",
                "eriddoch",
                "Eric",
                "Riddoch",
                "m",
                "11111",
                "22222",
                "33333");

        db.createTables();

        // generate some events
        event1 = Event.generateBirthEvent(1000, person);
        event2 = Event.generateDeathEvent(1000, person);
        event3 = Event.generateBirthEvent(2000, person);

        // insert them into database
        Connection conn = db.openConnection();
        EventDAO eDao = new EventDAO(conn);
        eDao.insert(event1);
        eDao.insert(event2);
        eDao.insert(event3);
        db.closeConnection(true);



    }

    @After
    public void tearDown() throws Exception {
        db.clearTables();
    }

    @Test
    public void getEventPASS() throws Exception {

        // perform event request
        Connection conn = db.openConnection();
        EventDAO eDao = new EventDAO(conn);
        Event compareTest = EventService.getEvent(event1.getEventID());
        db.closeConnection(true);

        // check to see that the results match
        Assert.assertEquals(event1, compareTest);

    }

    @Test
    public void getEventFAIL() throws DataAccessException {

        // delete events from table
        Connection conn = db.openConnection();
        EventDAO eDao = new EventDAO(conn);
        eDao.delete();
        db.closeConnection(true);

        // try to find an event that isn't there
        Event found = EventService.getEvent(event1.getEventID());
        Assert.assertNull(found);
    }

    @Test
    public void getEventsPASS() throws Exception {

        // get response from events service
        EventMultipleResponse response = EventService.getEvents("eriddoch");

        // convert response to json
        Gson gson = new Gson();
        String correctJson = gson.toJson(response, EventMultipleResponse.class);

        // use our serializing function to do the same thing
        String serviceJson = response.serialize();

        // check that they are the same
        Assert.assertEquals(correctJson, serviceJson);

    }

    @Test public void getEventsFAIL() throws DataAccessException {

        // try to call for nonexistent user
        EventMultipleResponse response = EventService.getEvents("kameron");

        // resulting response should have no events
        Assert.assertTrue(response.getData().size() == 0);

    }

}