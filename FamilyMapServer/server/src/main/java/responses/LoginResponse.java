package responses;

import requests.JsonPacket;

/**
 *
 * Serializes and deserializes Login responses
 *
 * Created by eric on 2/13/19.
 */
public class LoginResponse extends JsonPacket {

    private String authToken; /** Non-empty auth token string */
    private String userName;  /** Non-empty userName who made the request */
    private String personID;  /** Non-empty personID of user's generated Person */

    public LoginResponse(String authToken, String userName, String personID) {
        this.authToken = authToken;
        this.userName = userName;
        this.personID = personID;
    }

    public String getAuthToken() {
        return authToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoginResponse)) return false;

        LoginResponse that = (LoginResponse) o;

        if (getAuthToken() != null ? !getAuthToken().equals(that.getAuthToken()) : that.getAuthToken() != null)
            return false;
        if (getUserName() != null ? !getUserName().equals(that.getUserName()) : that.getUserName() != null)
            return false;
        return getPersonID() != null ? getPersonID().equals(that.getPersonID()) : that.getPersonID() == null;
    }

    @Override
    public int hashCode() {
        int result = getAuthToken() != null ? getAuthToken().hashCode() : 0;
        result = 31 * result + (getUserName() != null ? getUserName().hashCode() : 0);
        result = 31 * result + (getPersonID() != null ? getPersonID().hashCode() : 0);
        return result;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
}
