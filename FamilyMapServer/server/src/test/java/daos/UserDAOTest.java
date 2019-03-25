package daos;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import models.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by eric on 2/16/19.
 */
public class UserDAOTest {

    Database db;
    User user;
    User user2;
    User user3;
    ArrayList<User> users;

    @Before
    public void setUp() throws Exception {
        db = new Database();
        user = new User(
                "eriddoch",
                "P@SSW0RD!!",
                "eric.riddoch@gmail.com",
                "Eric",
                "Riddoch",
                "m",
                "11111"
        );
        db.createTables();

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

        users = new ArrayList<User>();
        users.add(user);
        users.add(user2);
        users.add(user3);
    }

    @After
    public void tearDown() throws Exception {
        db.clearTables();
    }

    @Test
    public void insertPass() throws Exception {

        User compareTest = null;
        db.clearTables();
        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);

            uDao.insert(user);
            compareTest = uDao.find(user.getUserName());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNotNull(compareTest);
        assertEquals(user, compareTest);

    }

    @Test
    public void insertFail() throws Exception {
        boolean didItWork = true;

        // try to insert the same user twice
        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            uDao.insert(user);
            uDao.insert(user);
            db.closeConnection(didItWork);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            didItWork = false;
        }
        assertFalse(didItWork);

        // try to find user (shouldn't be there, the db did not commit)
        User compareTest = user;
        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            compareTest = uDao.find(user.getUserName());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNull(compareTest);

    }

    @Test
    public void findPass() throws Exception {

        User foundUser = null;

        // insert an user into the table
        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            uDao.insert(user);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        // try to find it
        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            foundUser = uDao.find(user.getUserName());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        Assert.assertEquals(user, foundUser);

    }

    @Test
    public void findFail() throws Exception {

        User foundUser = null;
        String fakeUserName = "i'm fake!";

        // try to find a nonexistent user
        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            foundUser = uDao.find(fakeUserName);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        // event should be null
        Assert.assertNull(foundUser);

    }

    @Test
    public void deletePass() throws DataAccessException {

        User foundUser = null;

        try {
            // insert a person into the database
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            uDao.insert(user);

            // retrieve the person
            foundUser = uDao.find(user.getUserName());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        // verify the person is in the database
        Assert.assertEquals(user, foundUser);

        try {
            // delete the entire people table
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            uDao.delete();

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        // attempt to retrieve the person (should return null)
        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            foundUser = uDao.find(user.getUserName());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        Assert.assertNull(foundUser);

    }

    @Test(expected = daos.DataAccessException.class)
    public void deleteFail() throws DataAccessException {

        try {

            // delete the table from the database
            Connection conn = db.openConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("drop table if exists " + Database.USERS);
            db.closeConnection(true);

            // try to delete the non-existent table
            conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            uDao.delete();

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

        User compareTest = null;
        db.clearTables();
        try {


            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);

            // insert many
            uDao.insertMany(users);

            // attempt to find the first user
            compareTest = uDao.find(user.getUserName());
            db.closeConnection(true);

        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNotNull(compareTest);
        assertEquals(user, compareTest);

    }

    @Test()
    public void insertManyFAIL() throws Exception {

        boolean didItWork = true;

        // try to insert the same users twice
        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            uDao.insertMany(users);
            uDao.insertMany(users);
            db.closeConnection(didItWork);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            didItWork = false;
        }
        assertFalse(didItWork);

        // try to find user (shouldn't be there, the db did not commit)
        User compareTest = user;
        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            compareTest = uDao.find(user.getUserName());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNull(compareTest);

    }

}