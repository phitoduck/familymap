package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import models.Model;
import models.Person;
import models.User;

/**
 * Manages insertion and access of users in a database.
 *
 * Created by eric on 2/13/19.
 */

public class UserDAO extends DAO {

    public UserDAO(Connection connection) {
        super(connection);
    }

    /**
     * @param toInsert User to be inserted as a row into the database.
     * @return true if no errors were thrown while attempting to insert.
     * @throws DataAccessException if the insertion fails at any point.
     * */
    public boolean insert(User toInsert) throws DataAccessException {

        boolean commit = true; // not true if error
        String sql = "INSERT INTO " + Database.USERS + " (userName, password, email, first_name, " +
                "last_name, gender, person_id)" +
                " VALUES(?,?,?,?,?,?,?)";

        try {

            PreparedStatement stmt = conn.prepareStatement(sql);

            // insert model data into statement
            stmt.setString(1, toInsert.getUserName());
            stmt.setString(2, toInsert.getPassword());
            stmt.setString(3, toInsert.getEmail());
            stmt.setString(4, toInsert.getFirstName());
            stmt.setString(5, toInsert.getLastName());
            stmt.setString(6, toInsert.getGender());
            stmt.setString(7, toInsert.getPersonID());

            stmt.executeUpdate(); // insert into database

        } catch (SQLException e) {
            commit = false;
            throw new DataAccessException(e.getMessage());
        }

        return commit;
    }

    /**
     *
     * Inserts many users into the database
     *
     * @param users
     * @return
     */
    public boolean insertMany(Collection<User> users) throws DataAccessException {

        for (User user : users) {
            insert(user);
        }

        return true;

    }
    /**
     *
     * @param modelIDs An array list of all model primary keys in table that we wish to find
     * @return A SQL ResultSet of the models that we found in the table
     * @throws DataAccessException any SQLite error during deletion causes this to be thrown
     */
    public ResultSet findMany(ArrayList<String> modelIDs) throws DataAccessException { return null; }

    @Override
    public User find(String userName) throws DataAccessException {

        User user = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM " + Database.USERS + " WHERE userName = ?";

        // attempt to find the model
        try {

            // establish db connection and stmt
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,userName);
            rs = stmt.executeQuery();
            if (rs.next() == true) { // create person if result set is not empty
                user = new User(
                        rs.getString("userName"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("gender"),
                        rs.getString("person_id"));
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error occurred while finding the model");
        }

        return user; // null if error thrown
    }

    @Override
    public void delete() throws DataAccessException {

        // drop all entries from each table
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("delete from " + Database.USERS);
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }

    }

    /**
     *
     * Delete user with designated username from table
     *
     * @param userName
     * @throws DataAccessException
     */
    public void delete(String userName) throws DataAccessException {

        // delete user from table
        try {
            // delete all events associated with user
            String sql = "delete from " + Database.USERS + " where userName = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userName);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }

    }

}
