package daos;

/**
 * Exception thrown for errors relating to inserting or finding a data model in a database.
 *
 * Created by eric on 2/13/19.
 */

public class DataAccessException extends Exception {
    public DataAccessException(String message) {
        super(message);
    }
}
