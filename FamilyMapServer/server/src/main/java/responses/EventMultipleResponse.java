package responses;

import java.util.ArrayList;

import models.Event;
import requests.JsonPacket;

/**
 * Created by eric on 2/15/19.
 */

public class EventMultipleResponse extends JsonPacket {

    private ArrayList<Event> data;

    public EventMultipleResponse(ArrayList<Event> data) {
        this.data = data;
    }

    public ArrayList<Event> getData() {
        return data;
    }
}
