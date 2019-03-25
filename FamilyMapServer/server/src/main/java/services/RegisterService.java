package services;

import java.sql.Connection;

import daos.DataAccessException;
import daos.Database;
import daos.PersonDAO;
import daos.UserDAO;
import models.Person;
import models.User;
import requests.LoginRequest;
import requests.RegisterRequest;
import responses.LoginResponse;

/**
 *
 * Fills Register requests
 *
 * Created by eric on 2/13/19.
 */

public class RegisterService extends Service {

    /**
     *
     * @param request
     * @return RegisterResponse object
     * @throws DataAccessException
     */
    public static LoginResponse register(RegisterRequest request)
            throws DataAccessException {

        // only true if no errors are thrown
        boolean commit = false;
        Database db = new Database();
        LoginResponse response = null;
        User user = null;
        Person userPerson = null;

        try {

            // 1. connect to database
            Connection conn = db.openConnection();

            // 2. create a person for the user
            userPerson = new Person(
                    request.getUserName(),
                    request.getFirstName(),
                    request.getLastName(),
                    request.getGender(),
                    "",
                    "",
                    ""
            );

            // 3. create a user
            user = new User(
                    request.getUserName(),
                    request.getPassword(),
                    request.getEmail(),
                    request.getFirstName(),
                    request.getLastName(),
                    request.getGender(),
                    userPerson.getPersonID()
            );

            // 4. Insert user and person into the database
            UserDAO uDao = new UserDAO(conn);
            PersonDAO pDao = new PersonDAO(conn);
            pDao.insert(userPerson);
            uDao.insert(user);

            commit = true;
        } finally {
            db.closeConnection(commit);
        }

        // 5. Login the user
        LoginRequest loginRequest = new LoginRequest(
                user.getUserName(),
                user.getPassword()
        );

        response = LoginService.login(loginRequest);

        return response;
    }

}
