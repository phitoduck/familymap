package daos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

import models.Model;

/**
 * Created by eric on 2/13/19.
 */

public abstract class DAO {

    protected Connection conn;

    /**
     * @param connection A connection to a database in which the data of interest is housed.
     * */
    public DAO(Connection connection) {
        this.conn = connection;
    }

    /**
     * @param modelID ID Number of the model to lookup in its respective table in the database.
     * @throws DataAccessException if the access to the database fails at any point
     */
    public abstract Model find(String modelID) throws DataAccessException;


    /**
     * Delete the entire contents of the table associated with this DAO
     */
    public abstract void delete() throws DataAccessException;

}
