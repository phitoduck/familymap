package services;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import daos.DataAccessException;
import daos.Database;
import daos.UserDAO;
import models.User;
import requests.LoginRequest;
import responses.LoginResponse;

/**
 * Created by eric on 2/20/19.
 */
public class LoginServiceTest {

    private Database db;
    private Connection conn;
    private LoginRequest  loginRequest;
    private LoginResponse loginResponse;
    private User user;

    @Before
    public void setUp() throws DataAccessException {

        user = new User(
                "eriddoch",
                "P@SSW0RD!!",
                "eric.riddoch@gmail.com",
                "Eric",
                "Riddoch",
                "m",
                "11111"
        );

        loginRequest = new LoginRequest(
                "eriddoch",
                "P@SSW0RD!!"
        );

        loginResponse = new LoginResponse(
                "dummy token",
                "eriddoch",
                "11111"
        );

        db = new Database();

        // initialize database
        db.createTables();
        Connection conn = db.openConnection();

        // insert user into database
        UserDAO uDao = new UserDAO(conn);
        uDao.delete();
        uDao.insert(user);

        db.closeConnection(true);

    }

    @After
    public void tearDown() throws DataAccessException {
        db.clearTables();
    }

    @Test
    public void loginPass() throws Exception {

        LoginResponse compareTest = null;
        compareTest = LoginService.login(loginRequest);
        Assert.assertTrue(
                loginResponse.getUserName().equals(compareTest.getUserName()));
        Assert.assertTrue(
                loginResponse.getPersonID().equals(compareTest.getPersonID()));

    }

    @Test(expected = DataAccessException.class)
    public void loginFail() throws Exception {

        loginRequest.setPassword("wrong-password-2019");
        LoginService.login(loginRequest);

    }

}