package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.sql.Connection;

import daos.DataAccessException;
import models.Event;
import models.Person;
import responses.BasicResponse;
import responses.EventMultipleResponse;
import responses.PersonMultipleResponse;
import services.EventService;
import services.PersonService;

/**
 *
 * Handles Event requests
 *
 * Created by eric on 2/13/19.
 */

public class EventHandler extends Handler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        boolean success = false;

        try {

            System.out.println("Event handler ran!");

            // only fill request if "POST"
            if (httpExchange.getRequestMethod().toLowerCase().equals("get")) {

                // authenticate / retrieve username
                Headers headers = httpExchange.getRequestHeaders();
                String authToken = headers.getFirst("Authorization");
                String username = Handler.authenticate(authToken);

                // get eventID
                String path = httpExchange.getRequestURI().getPath();
                System.out.println(path);
                String[] uriArray = path.split("/");
                String eventID = null;

                try {
                    // attempt to parse the last
                    eventID = uriArray[uriArray.length - 1];
                } catch (Exception e) {
                    throw new DataAccessException("'Person' failed. " +
                            "Could not parse personID or number of people.");
                }

                // print feedback
                System.out.println("EventID " + eventID);

                // run register request
                String responseJson = null;
                if (eventID.toLowerCase().equals("event")) { // multi event request

                    // fill people request
                    EventMultipleResponse multiResponse = null;
                    multiResponse = EventService.getEvents(username);

                    // serialize
                    responseJson = multiResponse.serialize();

                } else { // single event request

                    // get associated event
                    Event eventResponse = EventService.getEvent(eventID);

                    // ensure that this eventID belongs to the user with the auth token
                    if (!username.equals(eventResponse.getUserName())) {
                        throw new DataAccessException("This event does not belong to " + username);
                    }

                    // serialize
                    Gson gson = new Gson();
                    responseJson = gson.toJson(eventResponse, Event.class);

                }

                // send OK http response key
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                // send response
                OutputStream responseBody = httpExchange.getResponseBody();
                Handler.writeString(responseJson, responseBody);

                // close http transaction
                responseBody.close();

                success = true;

            }

            if (!success) {
                throw new DataAccessException("Event request failed");
            }

        } catch (Exception e) {

            // send BAD REQUEST http response key
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

            // send json error response
            BasicResponse errorResponse = new BasicResponse(e.getMessage());
            String errorJson = errorResponse.serialize();
            OutputStream responseBody = httpExchange.getResponseBody();
            Handler.writeString(errorJson, responseBody);

            // close http transaction
            responseBody.close();

        }

    }
}
