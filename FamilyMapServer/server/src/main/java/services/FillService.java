package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import daos.DataAccessException;
import daos.Database;
import daos.PersonDAO;
import daos.UserDAO;
import familytree.FamilyTree;
import models.Person;
import models.User;
import responses.BasicResponse;

/**
 *
 * Fills Fill Requests.
 *
 * Created by eric on 2/13/19.
 */
public class FillService extends Service {

    /**
     *
     * @param generations number of generations to generate
     * @return FillResponse object
     * @throws DataAccessException
     */
    public static BasicResponse fill(String userName, int generations)
            throws DataAccessException {

        // 1. delete every person and event connected to the user
        deleteUserPeople(userName);

        // 2. get the user object and user person
        Database db = new Database();
        Connection conn = db.openConnection();
        UserDAO uDao = new UserDAO(conn);
        User user = uDao.find(userName);
        db.closeConnection(true);

        if (user == null) {
            throw new DataAccessException(userName + " is not a registered user!");
        }

        Person userPerson = Person.generateUserPerson(user);
        user.setPersonID(userPerson.getPersonID());

        // 3. create generations for user
        FamilyTree familyTree = new FamilyTree(userPerson);
        familyTree.makeGenerations(generations);
        familyTree.commitTree();

        // 4. re-add user/person to database
        conn = db.openConnection();
        PersonDAO pDao = new PersonDAO(conn);
        uDao = new UserDAO(conn);
        uDao.delete(userName);
        uDao.insert(user);
        db.closeConnection(true);

        // 5. return a success response
        int numPersonsAdded = familyTree.getNumNodes();
        int numEventsAdded = familyTree.getNumEvents();

        return new BasicResponse("Successfully added " + numPersonsAdded +
                " persons and " + numEventsAdded + " events to the database.");
    }

    /**
     *
     * Deletes all people associated with the given user and their events
     *
     * @param userName
     * @throws DataAccessException
     */
    private static void deleteUserPeople(String userName) throws DataAccessException {
        Database db = new Database();
        Connection conn = db.openConnection();
        try {

            String sql = "delete from " + Database.PEOPLE + " where descendant = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userName);

            // delete all people associated with user
            stmt.executeUpdate();
            stmt.close();

            // delete all events associated with user
            sql = "delete from " + Database.EVENTS + " where userName = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userName);
            stmt.executeUpdate();

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            throw e;
        } catch (SQLException e) {
            db.closeConnection(false);
            throw new DataAccessException(e.getMessage());
        }
    }

}
