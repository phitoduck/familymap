package models;

/**
 * AuthToken object model. Contains the unique token and the userName associated with it.
 *
 * Created by eric on 2/13/19.
 */

public class AuthToken extends Model {

    private String token;
    private String userName;

    public AuthToken(String token, String userName) {
        this.token = token;
        this.userName = userName;
    }

    /** Autogenerates random token */
    public AuthToken(String userName) {
        this.userName = userName;
        this.token = Model.generateUuid(); // generate UUID for token
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthToken)) return false;

        AuthToken authToken = (AuthToken) o;

        if (!getToken().equals(authToken.getToken())) return false;
        return getUserName().equals(authToken.getUserName());
    }

    @Override
    public int hashCode() {
        int result = getToken().hashCode();
        result = 31 * result + getUserName().hashCode();
        return result;
    }
}
