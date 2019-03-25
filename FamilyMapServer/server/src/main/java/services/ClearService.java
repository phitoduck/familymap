package services;

import java.sql.Connection;

import daos.DataAccessException;
import daos.Database;
import responses.BasicResponse;

/**
 *
 * Fills Service Requests
 *
 * Created by eric on 2/13/19.
 */

public class ClearService extends Service {

    /**
     * @return ClearResponse object
     * @throws DataAccessException
     */
    public static BasicResponse clear()
            throws DataAccessException {

        Database db = new Database();
        db.clearTables();

        return new BasicResponse("Clear succeeded!");

    }

}
