package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.sql.Connection;

import daos.AuthTokenDAO;
import daos.DataAccessException;
import models.Person;
import requests.JsonPacket;
import responses.BasicResponse;
import responses.PersonMultipleResponse;
import services.FillService;
import services.PersonService;

/**
 *
 * Handles Person requests
 *
 * Created by eric on 2/13/19.
 */

public class PersonHandler extends Handler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        boolean success = false;

        try {

            System.out.println("Person handler ran!");

            // only fill request if "POST"
            if (httpExchange.getRequestMethod().toLowerCase().equals("get")) {

                // authenticate / retrieve username
                Headers headers = httpExchange.getRequestHeaders();
                String authToken = headers.getFirst("Authorization");
                String username = Handler.authenticate(authToken);

                // get number of generations to generate
                String path = httpExchange.getRequestURI().getPath();
                System.out.println(path);
                String[] uriArray = path.split("/");
                String personID = null;

                try {
                    // attempt to parse the last
                    personID = uriArray[uriArray.length - 1];
                } catch (Exception e) {
                    throw new DataAccessException("'Person' failed. " +
                            "Could not parse personID or number of people.");
                }

                // print feedback
                System.out.println("PersonID" + personID);

                // run register request
                String responseJson = null;
                if (personID.toLowerCase().equals("person")) { // multi person request

                    // fill people request
                    PersonMultipleResponse multiResponse = null;
                    multiResponse = PersonService.getPeople(username);

                    // serialize
                    responseJson = multiResponse.serialize();

                } else { // single person request

                    // get associated person
                    Person personResponse = PersonService.getPerson(personID);

                    // ensure that this personID belongs to the user with the auth token
                    if (!username.equals(personResponse.getDescendant())) {
                        throw new DataAccessException("This person does not belong to " + username);
                    }

                    // serialize
                    Gson gson = new Gson();
                    responseJson = gson.toJson(personResponse, Person.class);

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
                throw new DataAccessException("Person request failed");
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
