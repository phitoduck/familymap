package responses;

import requests.JsonPacket;

/**
 *
 * Serializes and deserializes Person responses
 *
 * Created by eric on 2/13/19.
 */

public class PersonResponse extends JsonPacket {

    private String descendant; /** name of user account this person belongs to */
    private String personID;   /** Person's unique id */
    private String firstName;  /** Person's first name */
    private String lastName;   /** Person's last name */
    private String gender;     /** Person's gender */
    private String father;

    public PersonResponse(String descendant, String personID, String firstName,
                          String lastName, String gender, String father, String mother,
                          String spouse) {
        this.descendant = descendant;
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.father = father;
        this.mother = mother;
        this.spouse = spouse;
    }

    /** ID of person's father OPTIONAL */
    private String mother;     /** ID of person's mother OPTIONAL */
    private String spouse;     /** ID of person's spouse OPTIONAL */

}
