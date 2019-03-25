package daos;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import models.Person;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by eric on 2/16/19.
 */
public class PersonDAOTest {

    Database db;
    Person person;
    Person person2;
    Person person3;
    ArrayList<Person> persons;

    @Before
    public void setUp() throws Exception {
        db = new Database();
        person = new Person(
                "00000",
                "eriddoch",
                "Eric",
                "Riddoch",
                "m",
                "11111",
                "22222",
                "33333");
        db.createTables();

        person2 = new Person(
                "000003321",
                "eriddoch123",
                "Eric",
                "Riddoch",
                "m",
                "11111",
                "22222",
                "33333");

        person3 = new Person(
                "000002",
                "eriddoch2123",
                "Eric",
                "Riddoch",
                "m",
                "11111",
                "22222",
                "33333");

        persons = new ArrayList<>();
        persons.add(person);
        persons.add(person2);
        persons.add(person3);
    }

    @After
    public void tearDown() throws Exception {
        db.clearTables();
    }

    @Test
    public void insertPass() throws Exception {

        // insert person and retrieve it
        Person compareTest = null;
        db.clearTables();
        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);

            pDao.insert(person);
            compareTest = pDao.find(person.getPersonID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        // was it retrieved?
        assertNotNull(compareTest);

        // does it match?
        assertEquals(person, compareTest);

    }

    @Test
    public void insertFail() throws Exception {

        boolean didItWork = true;

        // try to insert the same person twice
        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            pDao.insert(person);
            pDao.insert(person);
            db.closeConnection(didItWork);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            didItWork = false;
        }

        // should not have worked
        assertFalse(didItWork);

        // try to find person (shouldn't be there, the db did not commit)
        Person compareTest = null;
        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            compareTest = pDao.find(person.getPersonID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNull(compareTest);

    }

    @Test
    public void findPass() throws Exception {

        Person foundPerson = null;

        // insert a person into the table
        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            pDao.insert(person);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        // try to find it
        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            foundPerson = pDao.find(person.getPersonID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        Assert.assertEquals(person.toString(), foundPerson.toString());

    }

    @Test
    public void findFail() throws Exception {

        Person foundPerson = null;
        String fakeID = "i'm fake!";

        // try to find a nonexistent person
        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            foundPerson = pDao.find(fakeID);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        // event should be null
        Assert.assertNull(foundPerson);

    }

    @Test
    public void deletePass() throws DataAccessException {

        Person foundPerson = null;

        try {
            // insert a person into the database
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            pDao.insert(person);

            // retrieve the person
            foundPerson = pDao.find(person.getPersonID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        // verify the person is in the database
        Assert.assertEquals(person, foundPerson);

        try {
            // delete the entire people table
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            pDao.delete();

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        // attempt to retrieve the person (should return null)
        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            foundPerson = pDao.find(person.getPersonID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        Assert.assertNull(foundPerson);

    }

    @Test(expected = daos.DataAccessException.class)
    public void deleteFail() throws DataAccessException {

        try {

            // delete the table from the database
            Connection conn = db.openConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("drop table if exists " + Database.PEOPLE);
            db.closeConnection(true);

            // try to delete the non-existent table
            conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            pDao.delete();

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


        // insert persons and retrieve it
        Person compareTest = null;
        db.clearTables();
        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);

            // insert many
            pDao.insertMany(persons);
            compareTest = pDao.find(person.getPersonID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        // was it retrieved?
        assertNotNull(compareTest);

        // does it match?
        assertEquals(person, compareTest);
    }

    @Test()
    public void insertManyFAIL() throws Exception {

        boolean didItWork = true;

        // try to insert the same persons twice
        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            pDao.insertMany(persons);
            pDao.insertMany(persons);
            db.closeConnection(didItWork);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            didItWork = false;
        }

        // should not have worked
        assertFalse(didItWork);

        // try to find person (shouldn't be there, the db did not commit)
        Person compareTest = null;
        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            compareTest = pDao.find(person.getPersonID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNull(compareTest);

    }

}