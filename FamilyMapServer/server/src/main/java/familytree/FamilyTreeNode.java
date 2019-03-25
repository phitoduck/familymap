package familytree;

import java.sql.Connection;
import java.util.ArrayList;

import daos.DataAccessException;
import daos.EventDAO;
import daos.PersonDAO;
import models.Event;
import models.Person;

/**
 * Contains a person and all of their events
 *
 * Created by eric on 2/22/19.
 */

public class FamilyTreeNode {

    private Person person;
    private ArrayList<Event> events;
    private FamilyTreeNode father;
    private FamilyTreeNode mother;
    private FamilyTreeNode spouse;

    private int birthYear;

    public FamilyTreeNode(Person person) {
        this.person = person;
        this.events = new ArrayList<>();
        this.father = null;
        this.mother = null;
        this.spouse = null;
    }

    /**
     * Commits the person and all associated events to the database
     */
    public void addToDatabase(Connection conn) throws DataAccessException {

            // insert person into database
            PersonDAO pDao = new PersonDAO(conn);
            pDao.insert(person);

            // insert events into database
            EventDAO eDao = new EventDAO(conn);
            for (Event event : events) {
                eDao.insert(event);
            }

    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public FamilyTreeNode getFather() {
        return father;
    }

    public void setFather(FamilyTreeNode father) {
        this.father = father;
    }

    public FamilyTreeNode getMother() {
        return mother;
    }



    public String getLastName() {
        return person.getLastName();
    }

    public void setMother(FamilyTreeNode mother) {
        this.mother = mother;
    }

    public FamilyTreeNode getSpouse() {
        return spouse;
    }

    public void setSpouse(FamilyTreeNode spouse) {
        this.spouse = spouse;
    }

    public void addEvent(Event event) {
        this.events.add(event);
    }

    public String getFatherID() {
        return this.person.getFather();
    }

    public String getMotherID() {
        return this.person.getMother();
    }

    public String getSpouseID() {
        return this.person.getSpouse();
    }

    public String getPersonID() {
        return this.person.getPersonID();
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}
