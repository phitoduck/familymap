package services;


import java.sql.Connection;
import java.util.ArrayList;

import daos.DataAccessException;
import daos.Database;
import daos.EventDAO;
import models.Event;
import responses.EventMultipleResponse;

/**
 *
 * Fills Event requests
 *
 * Created by eric on 2/13/19.
 */

public class EventService extends Service {

    /**
     *
     * Retrieve the event of the given Id
     *
     * @param eventID id of event to retrieve
     * @return EventResponse object
     * @throws DataAccessException
     */
    public static Event getEvent(String eventID)
            throws DataAccessException {

        Database db = new Database();
        Connection conn = db.openConnection();
        Event toReturn = null;

        try {

            EventDAO eDao = new EventDAO(conn);
            toReturn = eDao.find(eventID);

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
     * Retrieve all events associated with the given user
     *
     * @param userName userName of user whose events are to be retrieved
     * @return EventResponse object
     * @throws DataAccessException
     */
    public static EventMultipleResponse getEvents(String userName)
            throws DataAccessException {

        Database db = new Database();
        Connection conn = db.openConnection();
        ArrayList<Event> toReturn = null;

        try {

            EventDAO eDao = new EventDAO(conn);
            toReturn = eDao.findAll(userName);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
            db.closeConnection(false);
            throw e;
        }

        return new EventMultipleResponse(toReturn);

    }



}
