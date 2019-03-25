package familytree;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import daos.DataAccessException;
import daos.Database;
import daos.PersonDAO;
import models.Person;
import models.User;

/**
 * Created by eric on 2/22/19.
 */
public class FamilyTreeTest {

    private Connection conn;
    private Database db;
    private Person userPerson;
    private User user;

    @Before
    public void setUp() throws Exception {
        db = new Database();
        db.createTables();

        user = new User(
                "eriddoch",
                "P@SSW0RD",
                "eric.riddoch@gmail.com",
                "Eric",
                "Riddoch",
                "m",
                "none");

        userPerson = Person.generateUserPerson(user);
        user.setPersonID(userPerson.getPersonID());

    }

    @After
    public void tearDown() throws DataAccessException {
        db.clearTables();
    }

    // This is an old test that was created
    // when addSpousalPair was still responsible for
    // committing the tree to the database

//    @Test
//    public void addSpousalPairPASS() throws DataAccessException {
//
//        conn = db.openConnection();
//
//        // create a node to test
//        FamilyTreeNode node = new FamilyTreeNode(userPerson);
//
//        // create a family tree ONLY so we can call the function
//        FamilyTree tree = new FamilyTree(userPerson);
//        tree.addSpousalPair(node);
//
//        // check that father and mother exist
//        FamilyTreeNode father = node.getFather();
//        FamilyTreeNode mother = node.getMother();
//        Assert.assertNotNull(father);
//        Assert.assertNotNull(mother);
//
//        // check that spouse id's are correct
//        Assert.assertEquals(father.getSpouseID(), mother.getPersonID());
//        Assert.assertEquals(mother.getSpouseID(), father.getPersonID());
//
//        // check that the parent id's are correct
//        Assert.assertEquals(node.getFatherID(), father.getPersonID());
//        Assert.assertEquals(node.getMotherID(), mother.getPersonID());
//
//        // check that all three are in the database
//        PersonDAO pDao = new PersonDAO(conn);
//        Person foundMother = pDao.find(mother.getPersonID());
//        Person foundFather = pDao.find(father.getPersonID());
//        Assert.assertNotNull(foundFather);
//        Assert.assertNotNull(foundMother);
//
//        // check that they match the inserted people
//        Assert.assertEquals(mother.getPerson(), foundMother);
//        Assert.assertEquals(father.getPerson(), foundFather);
//
//        db.closeConnection(false);
//
//    }

    @Test
    public void makeGenerationsPASS() throws DataAccessException {

        // create 4 generations on the family tree
        FamilyTree tree = new FamilyTree(userPerson);
        tree.makeGenerations(4);

        // test for correct number of nodes
        Assert.assertEquals(31, tree.getNumNodes());

    }


}