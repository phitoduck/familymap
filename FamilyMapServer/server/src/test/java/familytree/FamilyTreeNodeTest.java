package familytree;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import daos.DataAccessException;
import daos.Database;
import daos.EventDAO;
import daos.PersonDAO;
import models.Event;
import models.Model;
import models.Person;
import models.User;

/**
 * Created by eric on 2/22/19.
 */
public class FamilyTreeNodeTest {

    private Database db;
    private User user;
    private Person person;
    private FamilyTreeNode node;

    @Before
    public void setUp() {
        try {
            db = new Database();
            db.clearTables();
            db.createTables();
        } catch(DataAccessException e) {
            e.printStackTrace();
        }
        user = new User("eriddoch", "KingdomHeartsRulez",
                "howdy.com", "Eric", "Riddoch",
                "m", Model.generateUuid());
        person = Person.generateUserPerson(user);
        node = new FamilyTreeNode(person);
        node.addEvent(Event.generateBirthEvent(2020, person));
    }

    @Test
    public void addToDatabasePass() throws DataAccessException {
        Database db = new Database();

        Connection conn = db.openConnection();
        try {

            // add a node to the database
            node.addToDatabase(conn);

            PersonDAO personDao = new PersonDAO(conn);
            EventDAO eventDao = new EventDAO(conn);

            // find the models in the database
            Event compareEvent = eventDao.find(node.getEvents().get(0).getEventID());
            Person comparePerson = personDao.find(person.getPersonID());

//            // check that they are equal
            Assert.assertEquals(compareEvent, node.getEvents().get(0));
            Assert.assertEquals(comparePerson, person);

            System.out.println(person.toString().equals(comparePerson.toString()));

            db.closeConnection(true);
        } catch(DataAccessException e) {
            db.closeConnection(false);
            e.printStackTrace();
        }
    }

}