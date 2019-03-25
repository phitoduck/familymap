package services;

import java.sql.Connection;
import java.util.ArrayList;

import daos.AuthTokenDAO;
import daos.DataAccessException;
import daos.Database;
import daos.EventDAO;
import daos.PersonDAO;
import models.AuthToken;
import models.Person;
import responses.EventMultipleResponse;
import responses.PersonMultipleResponse;
import responses.PersonResponse;

/**
 *
 * Fills Person requests
 *
 * Created by eric on 2/13/19.
 */

public class PersonService extends Service {

    /**
     *
     * @param personID person id of person object to retrieve
     * @return Person object
     * @throws DataAccessException
     */
    public static Person getPerson(String personID)
            throws DataAccessException {

        Database db = new Database();
        Connection conn = db.openConnection();
        Person toReturn = null;

        try {

            PersonDAO pDao = new PersonDAO(conn);
            toReturn = pDao.find(personID);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
            db.closeConnection(false);
            throw e;
        }

        return toReturn;
    }

    /**
     *
     * @param userName username of user whose family is to be retrieved
     * @return PersonMultipleResponse with all person objects related to user
     * @throws DataAccessException
     */
    public static PersonMultipleResponse getPeople(String userName)
            throws DataAccessException {

        Database db = new Database();
        Connection conn = db.openConnection();
        ArrayList<Person> toReturn = null;

        try {

            // get people from user
            PersonDAO pDao = new PersonDAO(conn);
            toReturn = pDao.findAll(userName);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
            db.closeConnection(false);
            throw e;
        }

        return new PersonMultipleResponse(toReturn);

    }

}
