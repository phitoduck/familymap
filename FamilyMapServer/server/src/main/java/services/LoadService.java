package services;

import java.sql.Connection;
import java.util.ArrayList;

import daos.DataAccessException;
import daos.Database;
import daos.EventDAO;
import daos.PersonDAO;
import daos.UserDAO;
import models.Event;
import models.Person;
import models.User;
import requests.LoadRequest;
import responses.BasicResponse;

/**
 *
 * Fills Load Requests
 *
 * Created by eric on 2/13/19.
 */

public class LoadService extends Service {

    /**
     * @param request
     * @return LoadResponse object
     * @throws DataAccessException
     */
    public static BasicResponse load(LoadRequest request)
            throws DataAccessException {

        // first clear the database
        ClearService.clear();

        ArrayList<User> users = request.getUsers();
        ArrayList<Person> people = request.getPeople();
        ArrayList<Event> events = request.getEvents();

        Database db = new Database();

        try {

            // insert all of them into the database
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            PersonDAO pDao = new PersonDAO(conn);
            EventDAO eDao = new EventDAO(conn);

            uDao.insertMany(users);
            pDao.insertMany(people);
            eDao.insertMany(events);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            throw e;
        }

        // return response
        int numUsers = users.size();
        int numPeople = people.size();
        int numEvents = events.size();

        return new BasicResponse(
                "Successfully returned " + numUsers + " users," +
                        numPeople + " persons, and " +
                        numEvents + " events."
        );

    }

}
