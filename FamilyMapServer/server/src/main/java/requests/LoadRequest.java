package requests;

import java.util.ArrayList;

import models.Event;
import models.Person;
import models.User;

/**
 *
 * Serializes and deserializes Load requests
 *
 * Created by eric on 2/13/19.
 */

public class LoadRequest extends JsonPacket {

    private ArrayList<User>   users;
    private ArrayList<Person> persons;
    private ArrayList<Event>  events;

    public LoadRequest(ArrayList<User> users, ArrayList<Person> persons, ArrayList<Event> events) {
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Person> getPeople() {
        return persons;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }
}
