package services;

import java.sql.Connection;

import daos.AuthTokenDAO;
import daos.DataAccessException;
import daos.Database;
import daos.UserDAO;
import models.AuthToken;
import models.User;
import requests.LoginRequest;
import responses.LoginResponse;

/**
 *
 * Fills Login requests
 *
 * Created by eric on 2/13/19.
 */
public class LoginService extends Service {

    /**
     *
     * @param request
     * @return LoginResponse object
     * @throws DataAccessException
     */
    public static LoginResponse login(LoginRequest request)
            throws DataAccessException {

        Database db = new Database();
        Connection conn = null;

        // only true if no errors are thrown
        boolean commit = false;

        LoginResponse response = null;

        try {
            // 1. Attempt to find user
            conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            User user = uDao.find(request.getUserName());

            if (user == null) {
                throw new DataAccessException("User not found!");
            }

            // 2. Check that password matches
            if (!user.getPassword().equals(request.getPassword())) {
                throw new DataAccessException("Wrong password!");
            }

            // 3. Add new auth token to database
            AuthToken authToken = new AuthToken(user.getUserName());
            AuthTokenDAO aDao = new AuthTokenDAO(conn);
            aDao.insert(authToken);

            // 4. Generate login response
            response = new LoginResponse(
                    authToken.getToken(),
                    user.getUserName(),
                    user.getPersonID());

            commit = true;
        } finally {
            db.closeConnection(commit);
        }

        return response;

    }

}
