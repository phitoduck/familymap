package models;

import daos.ResourcePack;

/**
 * Created by westenm on 2/4/19.
 */

public class Event extends Model {

    public static final String BIRTH = "Birth";
    public static final String MARRIAGE = "Marriage";
    public static final String DEATH = "Death";

    private String eventID;
    private String descendant;
    private String personID;
    private double latitude;
    private double longitude;
    private String country;
    private String city;
    private String eventType;
    private int year;

    public Event(String userName, String personID, double latitude, double longitude,
                 String country, String city, String eventType, int year) {
        this(
                Model.generateUuid(),
                userName,
                personID,
                latitude,
                longitude,
                country,
                city,
                eventType,
                year
        );
    }

    public Event(String eventID, String userName, String personID, double latitude, double longitude,
                 String country, String city, String eventType, int year) {
        this.eventID = eventID;
        this.descendant = userName;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }


    /**
     *
     * generate a Birth event given a birth year and a person
     *
     * @param year
     * @param person
     * @return
     */
    public static Event generateBirthEvent(int year, Person person) {
        Location birthPlace = ResourcePack.getRandomLocation();
        return new Event(
                person.getDescendant(),
                person.getPersonID(),
                birthPlace.getLatitude(),
                birthPlace.getLongitude(),
                birthPlace.getCountry(),
                birthPlace.getCity(),
                Event.BIRTH,
                year
        );
    }

    /**
     *
     * generate a Death Event given a death year and a person
     *
     * @param year
     * @param person
     * @return
     */
    public static Event generateDeathEvent(int year, Person person) {
        Location birthPlace = ResourcePack.getRandomLocation();
        return new Event(
                person.getDescendant(),
                person.getPersonID(),
                birthPlace.getLatitude(),
                birthPlace.getLongitude(),
                birthPlace.getCountry(),
                birthPlace.getCity(),
                Event.DEATH,
                year
        );
    }

    /**
     * Get the Event ID
     */
    public String getEventID() {
        return eventID;
    }


    /**
     * Set the Event ID
     */
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }


    /**
     * Get the user's name
     */
    public String getUserName() {
        return descendant;
    }


    /**
     * Set the user's name
     */
    public void setUserName(String userName) {
        this.descendant = userName;
    }


    /**
     * Get the Person's ID
     */
    public String getPersonID() {
        return personID;
    }


    /**
     * Set the Person's ID
     */
    public void setPersonID(String personID) {
        this.personID = personID;
    }


    /**
     * Get the Latitude
     */
    public double getLatitude() {
        return latitude;
    }


    /**
     * Set the Latitude
     */
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }


    /**
     * Get the Longitude
     */
    public double getLongitude() {
        return longitude;
    }


    /**
     * Set the Longitude
     */
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }


    /**
     * Get the Country
     */
    public String getCountry() {
        return country;
    }


    /**
     * Set the Country
     */
    public void setCountry(String country) {
        this.country = country;
    }


    /**
     * Get the City
     */
    public String getCity() {
        return city;
    }


    /**
     * Set the City
     */
    public void setCity(String city) {
        this.city = city;
    }


    /**
     * Get Event Type
     */
    public String getEventType() {
        return eventType;
    }


    /**
     * Set the Event Type
     */
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }


    /**
     * Get the Year
     */
    public int getYear() {
        return year;
    }


    /**
     * Set the Year
     */
    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof Event) {
            Event oEvent = (Event) o;
            if (oEvent.getEventID().equals(getEventID()) &&
                    oEvent.getUserName().equals(getUserName()) &&
                    oEvent.getPersonID().equals(getPersonID()) &&
//                    oEvent.getLatitude() == (getLatitude()) &&
//                    oEvent.getLongitude() == (getLongitude()) &&
                    oEvent.getCountry().equals(getCountry()) &&
                    oEvent.getCity().equals(getCity()) &&
                    oEvent.getEventType().equals(getEventType()) &&
                    oEvent.getYear() == (getYear())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventID='" + eventID + '\'' +
                ", descendant='" + descendant + '\'' +
                ", personID='" + personID + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", eventType='" + eventType + '\'' +
                ", year=" + year +
                '}';
    }
}
