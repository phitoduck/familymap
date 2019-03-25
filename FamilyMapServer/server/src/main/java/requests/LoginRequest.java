package requests;

/**
 *
 * Serializes and deserializes Login requests
 *
 * Created by eric on 2/13/19.
 */

public class LoginRequest extends JsonPacket {

    private String userName; /** Non-empty string */
    private String password;

    public LoginRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
