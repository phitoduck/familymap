package responses;

import requests.JsonPacket;

/**
 * Response containing only a message
 *
 * Created by eric on 2/15/19.
 */

public class BasicResponse extends JsonPacket {

    private String message;

    public BasicResponse(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BasicResponse)) return false;

        BasicResponse that = (BasicResponse) o;

        return message != null ? message.equals(that.message) : that.message == null;
    }

    @Override
    public int hashCode() {
        return message != null ? message.hashCode() : 0;
    }
}
