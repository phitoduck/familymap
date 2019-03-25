package responses;

import java.util.ArrayList;

import models.Person;
import requests.JsonPacket;

/**
 * Created by eric on 2/15/19.
 */

public class PersonMultipleResponse extends JsonPacket {

    private ArrayList<Person> data; /** Array of Person objects */

    public PersonMultipleResponse(ArrayList<Person> data) {
        this.data = data;
    }

    public ArrayList<Person> getData() {
        return data;
    }
}
