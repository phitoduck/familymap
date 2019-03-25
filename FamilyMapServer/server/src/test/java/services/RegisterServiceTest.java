package services;


import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import daos.AuthTokenDAO;
import daos.DataAccessException;
import daos.Database;
import models.AuthToken;
import requests.RegisterRequest;
import responses.LoginResponse;

/**
 * Created by eric on 2/22/19.
 */
public class RegisterServiceTest {

    private Database db;
    private RegisterRequest registerRequest;

    @Before
    public void setUp() throws DataAccessException {

        registerRequest = new RegisterRequest(
                "eriddoch",
                "P@SSW0RD!!",
                "eric.riddoch@gmail.com",
                "Eric",
                "Riddoch",
                "m"
        );

        db = new Database();

        // initialize database
        db.createTables();

    }

    @After
    public void tearDown() throws DataAccessException {
        db.clearTables();
    }

    @Test
    public void registerPass() throws DataAccessException {

        // 1. register the user
        LoginResponse loginResponse = null;
        loginResponse = RegisterService.register(registerRequest);

        // 2. check that userName in response matches
        String userName = loginResponse.getUserName();
        Assert.assertEquals(registerRequest.getUserName(), userName);

        // 3. check for an auth token associated with that user
        Connection conn = db.openConnection();
        String token = loginResponse.getAuthToken();
        AuthTokenDAO aDao = new AuthTokenDAO(conn);
        AuthToken foundToken = aDao.find(token);
        db.closeConnection(true);
        // throws error if fails

        // 4. check that the auth token user matches the registered user
        Assert.assertEquals(userName, foundToken.getUserName());

    }

    @Test(expected = DataAccessException.class)
    public void registerFail() throws DataAccessException {

        // register the user twice
        LoginResponse loginResponse = null;
        RegisterService.register(registerRequest);
        RegisterService.register(registerRequest);

    }

}