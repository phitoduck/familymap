package services;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.util.ArrayList;

import daos.DataAccessException;
import daos.Database;
import daos.EventDAO;
import daos.PersonDAO;
import models.Event;
import models.Person;
import models.User;
import requests.LoadRequest;
import responses.BasicResponse;

/**
 * Created by eric on 2/23/19.
 */
public class LoadServiceTest {

    private BasicResponse correctResponse;
    private LoadRequest request;

    private Database db;
    private Person person;

    private Event event1;
    private Event event2;
    private Event event3;

    private Person person1;
    private Person person2;
    private Person person3;

    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp() throws Exception {
        db = new Database();
        person = new Person(
                "12345",
                "main event",
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

        db = new Database();
        person1 = new Person(
                "10000",
                "main person",
                "Eric",
                "Riddoch",
                "m",
                "11111",
                "22222",
                "33333");

        person2 = new Person(
                "10001",
                "main person",
                "Eric",
                "Riddoch",
                "m",
                "11111",
                "22222",
                "33333");

        person3 = new Person(
                "10002",
                "main person",
                "Eric",
                "Riddoch",
                "m",
                "11111",
                "22222",
                "33333");

        user1 = new User(
                "eriddoch1",
                "P@SSW0RD!!",
                "eric.riddoch@gmail.com",
                "Eric",
                "Riddoch",
                "m",
                "11111"
        );

        user2 = new User(
                "eriddoch2",
                "P@SSW0RD!!",
                "eric.riddoch@gmail.com",
                "Eric",
                "Riddoch",
                "m",
                "11111"
        );

        user3 = new User(
                "eriddoch3",
                "P@SSW0RD!!",
                "eric.riddoch@gmail.com",
                "Eric",
                "Riddoch",
                "m",
                "11111"
        );

        ArrayList<User> users = new ArrayList<>();
        ArrayList<Event> events = new ArrayList<>();
        ArrayList<Person> people = new ArrayList<>();

        users.add(user1);
        users.add(user2);
        users.add(user3);

        events.add(event1);
        events.add(event2);
        events.add(event3);

        people.add(person1);
        people.add(person2);
        people.add(person3);

        request = new LoadRequest(users, people, events);

    }

    @Test
    public void loadPASS() throws DataAccessException {

        // perform load service
        LoadService.load(request);

        Connection conn = db.openConnection();
        ArrayList<Person> foundPeople = null;
        ArrayList<Event> foundEvents = null;

        try {

            // find all events and people
            PersonDAO pDao = new PersonDAO(conn);
            EventDAO eDao = new EventDAO(conn);

            foundEvents = eDao.findAll("main event");
            foundPeople = pDao.findAll("main person");

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            throw e;
        }

        // check that all inserted people are the same
        Assert.assertEquals(request.getPeople(), foundPeople);
        Assert.assertEquals(request.getEvents(), foundEvents);

    }

    @Test(expected = DataAccessException.class)
    public void loadFAIL() throws DataAccessException {

        // try to insert multiple users with the same userName
        ArrayList<User> users = request.getUsers();
        users.get(0).setUserName("same");
        users.get(1).setUserName("same");
        users.get(2).setUserName("same");

        LoadService.load(request);

    }

}