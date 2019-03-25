package familytree;

import java.sql.Connection;
import java.util.ArrayList;

import daos.DataAccessException;
import daos.Database;
import daos.ResourcePack;
import models.Event;
import models.Location;
import models.Person;

/**
 * Created by eric on 2/22/19.
 */

public class FamilyTree {

    private static final int GENERATION_GAP = 25;
    private static final int MARRIAGE_OFFSET = 20;
    private static final int DEATH_OFFSET = 80;
    private static final int FIRST_BIRTH_YEAR = 2020;

    private String userName;
    private int numNodes;
    private int numEvents;

    private FamilyTreeNode root;

    public FamilyTree(Person person) {

        this.userName = person.getDescendant();
        this.numNodes = 1;
        this.numEvents = 0;

        // create the root person
        root = new FamilyTreeNode(person);

        // generate birth event
        Location birthPlace = ResourcePack.getRandomLocation();
        Event birth = new Event(
                person.getDescendant(),
                person.getPersonID(),
                birthPlace.getLatitude(),
                birthPlace.getLongitude(),
                birthPlace.getCountry(),
                birthPlace.getCity(),
                Event.BIRTH,
                FIRST_BIRTH_YEAR

        );
        root.addEvent(birth);
        this.numEvents++;
        root.setBirthYear(FIRST_BIRTH_YEAR);

    }

    public int getNumNodes() {
        return numNodes;
    }

    public int getNumEvents() {
        return numEvents;
    }

    public ArrayList<FamilyTreeNode> addSpousalPair(FamilyTreeNode child) throws DataAccessException {

        numNodes += 2;

        // 1. create father
        Person father = new Person(
                this.userName,
                ResourcePack.getRandomMaleName(),  // boy name
                child.getLastName(),               // same last name as child
                Person.MALE,
                "",
                "",
                ""
        );

        // 2. create mother to match
        Person mother = new Person(
                this.userName,
                ResourcePack.getRandomFemaleName(), // girl name
                ResourcePack.getRandomLastName(),   // generate maiden name
                Person.FEMALE,
                "",
                "",
                ""
        );

        // 3. link them to each other
        father.setSpouse(mother.getPersonID());
        mother.setSpouse(father.getPersonID());

        // 4. generate events for father and mother

        // birth
        int birthYear = child.getBirthYear() - GENERATION_GAP;
        Event fatherBirth = Event.generateBirthEvent(birthYear, father);
        Event motherBirth = Event.generateBirthEvent(birthYear, mother);

        // marriage
        int marriageYear = birthYear - MARRIAGE_OFFSET;
        Location marriagePlace = ResourcePack.getRandomLocation();
        Event fatherMarriage = new Event(
                this.userName,
                father.getPersonID(),
                marriagePlace.getLatitude(),
                marriagePlace.getLongitude(),
                marriagePlace.getCountry(),
                marriagePlace.getCity(),
                Event.MARRIAGE,
                marriageYear
        );

        Event motherMarriage = new Event(
                this.userName,
                mother.getPersonID(),
                marriagePlace.getLatitude(),
                marriagePlace.getLongitude(),
                marriagePlace.getCountry(),
                marriagePlace.getCity(),
                Event.MARRIAGE,
                marriageYear
        );

        // death
        int deathYear = birthYear - DEATH_OFFSET;
        Event fatherDeath = Event.generateDeathEvent(deathYear, father);
        Event motherDeath = Event.generateDeathEvent(deathYear, mother);

        // 5. build father and mother nodes
        FamilyTreeNode fatherNode = new FamilyTreeNode(father);
        FamilyTreeNode motherNode = new FamilyTreeNode(mother);
        fatherNode.addEvent(fatherBirth);
        fatherNode.addEvent(fatherMarriage);
        fatherNode.addEvent(fatherDeath);
        motherNode.addEvent(motherBirth);
        motherNode.addEvent(motherMarriage);
        motherNode.addEvent(motherDeath);
        this.numEvents += 6; // update # events associated with this tree

        // 6. link them to the child
        Person childPerson = child.getPerson();
        childPerson.setFather(father.getPersonID());
        childPerson.setMother(mother.getPersonID());
        child.setFather(fatherNode);
        child.setMother(motherNode);

        // 7. return array list of father and mother
        ArrayList<FamilyTreeNode> FM = new ArrayList<>();
        FM.add(fatherNode);
        FM.add(motherNode);
        return FM;

    }

    /**
     *
     * Generate a specified number of generations for the user and add
     * them to the database
     *
     * @param numGenerations
     * @throws DataAccessException
     */
    public void makeGenerations(int numGenerations) throws DataAccessException {

        if (numGenerations == 0) {
            return;
        }

        // add first generation
        ArrayList<FamilyTreeNode> currentGeneration = null;
        currentGeneration = addSpousalPair(root);

        // add next generations
        for (int i = 0; i < numGenerations - 1; i++) {
            ArrayList<FamilyTreeNode> nextGeneration = new ArrayList<>();
            for (FamilyTreeNode node : currentGeneration) {
                nextGeneration.addAll(addSpousalPair(node));
            }
            currentGeneration = nextGeneration;
        }

    }

    /**
     * Commit all people and their events in the tree into the database
     */
    public void commitTree() throws DataAccessException {

        ArrayList<FamilyTreeNode> toCommit = new ArrayList<>();

        // collect all nodes
        traverseTree(toCommit, root);

        // commit all nodes
        Database db = new Database();
        Connection conn = db.openConnection();

        try {
            for (FamilyTreeNode node : toCommit) {
                node.addToDatabase(conn);
            }
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            throw e;
        }

    }

    private void traverseTree(ArrayList<FamilyTreeNode> nodes, FamilyTreeNode node) {

        if (node == null) {
            return;
        }

        // add node to list
        nodes.add(node);

        // recurse on parents
        traverseTree(nodes, node.getFather());
        traverseTree(nodes, node.getMother());

    }

}
