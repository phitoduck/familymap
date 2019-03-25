package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import models.AuthToken;
import models.Person;

/**
 * Manages insertion and access of auth tokens in a database.
 *
 * Created by eric on 2/13/19.
 */

public class AuthTokenDAO extends DAO {

    public AuthTokenDAO(Connection connection) {
        super(connection);
    }

    /**
     * @param toInsert AuthToken to be inserted as a row into the database.
     * @return true if no errors were thrown while attempting to insert.
     * @throws DataAccessException if the insertion fails at any point.
     * */
    public boolean insert(AuthToken toInsert) throws DataAccessException {

        boolean commit = true; // not true if error
        String sql = "INSERT INTO " + Database.AUTH_TOKENS + " (token, userName)" +
                " VALUES(?,?)";

        try {

            PreparedStatement stmt = conn.prepareStatement(sql);

            // insert model data into statement
            stmt.setString(1, toInsert.getToken());
            stmt.setString(2, toInsert.getUserName());

            stmt.executeUpdate(); // insert into database

        } catch (SQLException e) {
            commit = false;
            throw new DataAccessException("Error occurred while inserting into the database");
        }

        return commit;
    }

    /**
     *
     * @param token token of the auth token to lookup in its respective table in the database.
     * @return AuthToken in the auth_tokens table with the specified ID, or null if AuthToken is not found
     */
    @Override
    public AuthToken find(String token) throws DataAccessException {
        AuthToken authToken = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM " + Database.AUTH_TOKENS + " WHERE token = ?";

        // attempt to find the model
        try {

            // establish db connection and stmt
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, token);
            rs = stmt.executeQuery();
            if (rs.next() == true) { // create person if result set is not empty
                authToken = new AuthToken(
                        rs.getString("token"),
                        rs.getString("userName"));
            }
            rs.close();

        } catch (SQLException e) {
            throw new DataAccessException("Error occurred while finding the model: " + e.getMessage());
        }

        return authToken; // null if error thrown
    }

    @Override
    public void delete() throws DataAccessException {

        // drop all entries from each table
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("delete from " + Database.AUTH_TOKENS);
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     *
     * Returns username associated with given auth token
     *
     * throws error if token is not in the table
     *
     * @param authToken
     * @return
     */
    public String authenticate(String authToken) throws DataAccessException {

        String username = null;

        try {

            // retrieve auth token model
            AuthToken tokenModel = this.find(authToken);
            if (tokenModel == null) {
                throw new DataAccessException("Invalid auth token");
            }

            username = tokenModel.getUserName();
            return username;

        } catch (DataAccessException e) {
            throw e;
        }


    }

}
