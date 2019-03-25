package models;

/**
 * Person data object. Stores information such as name, family relations, associated user, etc.
 *
 * Created by eric on 2/13/19.
 */

public class Person extends Model {

    public static final String MALE = "m";
    public static final String FEMALE = "f";

    private String personID;
    private String descendant;
    private String firstName;
    private String lastName;
    private String gender;
    private String father;
    private String mother;
    private String spouse;

    /** Constructor with randomly generated personID */
    public Person(String descendant, String firstName, String lastName, String gender,
                  String father, String mother, String spouse) {
        this.personID = Model.generateUuid();// generate UUID for person
        this.descendant = descendant;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender   = gender;
        this.father = father;
        this.mother = mother;
        this.spouse = spouse;
    }

    /** Constructor with custom personID */
    public Person(String personID, String descendant, String firstName, String lastName, String gender, String fatherID, String motherID, String spouseID) {
        this(
            descendant,
            firstName,
            lastName,
            gender,
            fatherID,
            motherID,
            spouseID
        );
        this.personID = personID;

    }

    /**
     *
     * Generates a person based on a user object.
     *
     * This assumes that the user's person ID will change to match
     * this person's id after it is generated.
     *
     * @param user
     * @return
     */
    public static Person generateUserPerson(User user) {
        return new Person(
                user.getUserName(),
                user.getFirstName(),
                user.getLastName(),
                user.getGender(),
                "",
                "",
                "");
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;

        Person person = (Person) o;

        if (!getPersonID().equals(person.getPersonID())) return false;
        if (!getDescendant().equals(person.getDescendant())) return false;
        if (!getFirstName().equals(person.getFirstName())) return false;
        if (!getLastName().equals(person.getLastName())) return false;
        if (!getGender().equals(person.getGender())) return false;
        if (!getFather().equals(person.getFather())) return false;
        if (!getMother().equals(person.getMother())) return false;
        return getSpouse().equals(person.getSpouse());
    }

    @Override
    public int hashCode() {
        int result = getPersonID().hashCode();
        result = 31 * result + getDescendant().hashCode();
        result = 31 * result + getFirstName().hashCode();
        result = 31 * result + getLastName().hashCode();
        result = 31 * result + getGender().hashCode();
        result = 31 * result + getFather().hashCode();
        result = 31 * result + getMother().hashCode();
        result = 31 * result + getSpouse().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Person{" +
                "personID='" + personID + '\'' +
                ", descendant='" + descendant + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", father='" + father + '\'' +
                ", mother='" + mother + '\'' +
                ", spouse='" + spouse + '\'' +
                '}';
    }

    public String getDescendant() {
        return descendant;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getMother() {
        return mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public String getSpouse() {
        return spouse;
    }

    public void setSpouse(String spouse) {
        this.spouse = spouse;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }


}
