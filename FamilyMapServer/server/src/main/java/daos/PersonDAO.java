package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import models.Person;

/**
 * Manages insertion and access of people in a database.
 *
 * Created by eric on 2/13/19.
 */

public class PersonDAO extends DAO {

    public PersonDAO(Connection connection) {
        super(connection);
    }

    /**
     * @param toInsert Person to be inserted as a row into the database.
     * @return true if no errors were thrown while attempting to insert.
     * @throws DataAccessException if the insertion fails at any point.
     * */
    public boolean insert(Person toInsert) throws DataAccessException {

        boolean commit = true; // not true if error
        String sql = "INSERT INTO " + Database.PEOPLE + " (person_id, descendant, first_name, " +
                "last_name, gender, father_id, mother_id, spouse_id)" +
                " VALUES(?,?,?,?,?,?,?,?)";

        try {

            PreparedStatement stmt = conn.prepareStatement(sql);

            // insert model data into statement
            stmt.setString(1, toInsert.getPersonID());
            stmt.setString(2, toInsert.getDescendant());
            stmt.setString(3, toInsert.getFirstName());
            stmt.setString(4, toInsert.getLastName());
            stmt.setString(5, toInsert.getGender());
            stmt.setString(6, toInsert.getFather());
            stmt.setString(7, toInsert.getMother());
            stmt.setString(8, toInsert.getSpouse());

            stmt.executeUpdate(); // insert into database

        } catch (SQLException e) {
            commit = false;
            throw new DataAccessException(e.getMessage());
        }

        return commit;
    }

    /**
     *
     * Inserts many Person objects into the database
     *
     * @param people
     * @return
     * @throws DataAccessException
     */
    public boolean insertMany(Collection<Person> people) throws DataAccessException {

        for (Person person : people) {
            insert(person);
        }

        return true;

    }

    /**
     *
     * @param personID ID Number of the person to lookup in its respective table in the database.
     * @return Person in the people table with the specified ID, or null if person is not found
     */
    @Override
    public Person find(String personID) throws DataAccessException {
        Person person = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM " + Database.PEOPLE + " WHERE person_id = ?";

        // attempt to find the model
        try {

            // establish db connection and stmt
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next() == true) { // create person if result set is not empty
                person = new Person(
                        rs.getString("person_id"),
                        rs.getString("descendant"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("gender"),
                        rs.getString("father_id"),
                        rs.getString("mother_id"),
                        rs.getString("spouse_id"));
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error occurred while finding the model");
        }

        return person; // null if error thrown

    }

    /**
     *
     * Find ALL of a user's person objects
     *
     * @param userName
     * @return ArrayList of all events associated with the given user
     */
    public ArrayList<Person> findAll(String userName) throws DataAccessException {

        ArrayList<Person> toReturn = new ArrayList<>();

        Person person = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM " + Database.PEOPLE + " WHERE descendant = ?;";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userName);
            rs = stmt.executeQuery();
            while (rs.next()) {
                person = new Person(
                        rs.getString("person_id"),
                        rs.getString("descendant"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("gender"),
                        rs.getString("father_id"),
                        rs.getString("mother_id"),
                        rs.getString("spouse_id"));
                toReturn.add(person);
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person");
        }

        return toReturn;
    }

    @Override
    public void delete() throws DataAccessException {

        // drop all entries from each table
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("delete from " + Database.PEOPLE);
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

}
