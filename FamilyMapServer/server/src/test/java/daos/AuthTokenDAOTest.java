package daos;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.crypto.Data;

import models.AuthToken;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by eric on 2/16/19.
 */
public class AuthTokenDAOTest {

    Database db;
    AuthToken authToken;


    @Before
    public void setUp() throws Exception {
        db = new Database();
        authToken = new AuthToken("eriddoch");
        db.createTables();
    }

    @After
    public void tearDown() throws Exception {
        db.clearTables();
    }

    @Test
    public void insertPass() throws Exception {

        AuthToken compareTest = null;
        db.clearTables();
        try {
            Connection conn = db.openConnection();
            AuthTokenDAO aDao = new AuthTokenDAO(conn);

            aDao.insert(authToken);
            compareTest = aDao.find(authToken.getToken());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNotNull(compareTest);
        assertEquals(authToken, compareTest);

    }

    @Test
    public void insertFail() throws Exception {
        boolean didItWork = true;

        // try to insert the same auth token twice
        try {
            Connection conn = db.openConnection();
            AuthTokenDAO aDao = new AuthTokenDAO(conn);
            aDao.insert(authToken);
            aDao.insert(authToken);
            db.closeConnection(didItWork);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            didItWork = false;
        }
        assertFalse(didItWork);

        // try to find auth token (shouldn't be there, the db did not commit)
        AuthToken compareTest = authToken;
        try {
            Connection conn = db.openConnection();
            AuthTokenDAO aDao = new AuthTokenDAO(conn);
            compareTest = aDao.find(authToken.getToken());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNull(compareTest);

    }

    @Test
    public void findPass() throws Exception {

        AuthToken foundToken = null;

        // insert an auth token into the table
        try {
            Connection conn = db.openConnection();
            AuthTokenDAO aDao = new AuthTokenDAO(conn);
            aDao.insert(authToken);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        // try to find it
        try {
            Connection conn = db.openConnection();
            AuthTokenDAO aDao = new AuthTokenDAO(conn);
            foundToken = aDao.find(authToken.getToken());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        Assert.assertEquals(authToken, foundToken);

    }

    @Test
    public void findFail() throws Exception {

        AuthToken foundToken = null;
        String fakeToken = "i'm fake!";

        // try to find a nonexistent event
        try {
            Connection conn = db.openConnection();
            AuthTokenDAO aDao = new AuthTokenDAO(conn);
            foundToken = aDao.find(fakeToken);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        // event should be null
        Assert.assertNull(foundToken);

    }

    @Test
    public void deletePass() throws DataAccessException {

        AuthToken foundToken = null;

        try {
            // insert a person into the database
            Connection conn = db.openConnection();
            AuthTokenDAO aDao = new AuthTokenDAO(conn);
            aDao.insert(authToken);

            // retrieve the person
            foundToken = aDao.find(authToken.getToken());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        // verify the person is in the database
        Assert.assertEquals(authToken, foundToken);

        try {
            // delete the entire people table
            Connection conn = db.openConnection();
            AuthTokenDAO aDao = new AuthTokenDAO(conn);
            aDao.delete();

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        // attempt to retrieve the person (should return null)
        try {
            Connection conn = db.openConnection();
            AuthTokenDAO aDao = new AuthTokenDAO(conn);
            foundToken = aDao.find(authToken.getToken());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        Assert.assertNull(foundToken);

    }

    @Test(expected = daos.DataAccessException.class)
    public void deleteFail() throws DataAccessException {

        try {

            // delete the table from the database
            Connection conn = db.openConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("drop table if exists " + Database.AUTH_TOKENS);
            db.closeConnection(true);

            // try to delete the non-existent table
            conn = db.openConnection();
            AuthTokenDAO aDao = new AuthTokenDAO(conn);
            aDao.delete();

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
    public void authenticatePASS() throws Exception {

        try {
            Connection conn = db.openConnection();
            AuthTokenDAO aDao = new AuthTokenDAO(conn);

            aDao.insert(authToken);

            Assert.assertEquals("eriddoch", aDao.authenticate(authToken.getToken()));

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

    }

    @Test(expected = DataAccessException.class)
    public void authenticateFAIL() throws Exception {

        try {
            Connection conn = db.openConnection();
            AuthTokenDAO aDao = new AuthTokenDAO(conn);

            aDao.insert(authToken);

            aDao.authenticate("fake token");

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            throw e;
        }

    }

}