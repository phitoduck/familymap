package services;

import com.google.gson.Gson;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import daos.DataAccessException;
import daos.Database;
import daos.PersonDAO;
import models.Person;
import responses.PersonMultipleResponse;

/**
 * Created by eric on 2/23/19.
 */
public class PersonServiceTest {

    private Database db;
    private Person person1;
    private Person person2;
    private Person person3;

    @Before
    public void setUp() throws Exception {
        db = new Database();
        person1 = new Person(
                "10000",
                "eriddoch",
                "Eric",
                "Riddoch",
                "m",
                "11111",
                "22222",
                "33333");

        person2 = new Person(
                "10001",
                "eriddoch",
                "Eric",
                "Riddoch",
                "m",
                "11111",
                "22222",
                "33333");

        person3 = new Person(
                "10002",
                "eriddoch",
                "Eric",
                "Riddoch",
                "m",
                "11111",
                "22222",
                "33333");

        db.createTables();

        // insert them into database
        Connection conn = db.openConnection();
        PersonDAO pDao = new PersonDAO(conn);
        pDao.insert(person1);
        pDao.insert(person2);
        pDao.insert(person3);
        db.closeConnection(true);

    }

    @After
    public void tearDown() throws Exception {
        db.clearTables();
    }

    @Test
    public void getPersonPASS() throws Exception {

        // perform event request
        Connection conn = db.openConnection();
        PersonDAO pDao = new PersonDAO(conn);
        Person compareTest = PersonService.getPerson(person1.getPersonID());
        db.closeConnection(true);

        // check to see that the results match
        Assert.assertEquals(person1, compareTest);

    }

    @Test
    public void getPeopleFAIL() throws DataAccessException {

        // delete events from table
        Connection conn = db.openConnection();
        PersonDAO pDao = new PersonDAO(conn);
        pDao.delete();
        db.closeConnection(true);

        // try to find an event that isn't there
        Person found = PersonService.getPerson(person1.getPersonID());
        Assert.assertNull(found);
    }

    @Test
    public void getEventsPASS() throws Exception {

        // get response from person service
        PersonMultipleResponse response = PersonService.getPeople("eriddoch");

        // convert response to json
        Gson gson = new Gson();
        String correctJson = gson.toJson(response, PersonMultipleResponse.class);

        // use our serializing function to do the same thing
        String serviceJson = response.serialize();

        // check that they are the same
        Assert.assertEquals(correctJson, serviceJson);

    }

    @Test public void getEventsFAIL() throws DataAccessException {

        // try to call for nonexistent user
        PersonMultipleResponse response = PersonService.getPeople("kameron");

        // resulting response should have no person objects
        Assert.assertTrue(response.getData().size() == 0);

    }

}