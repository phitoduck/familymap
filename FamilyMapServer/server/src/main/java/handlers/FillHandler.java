package handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.sql.Connection;

import daos.DataAccessException;
import requests.JsonPacket;
import requests.RegisterRequest;
import responses.BasicResponse;
import responses.LoginResponse;
import services.FillService;
import services.RegisterService;

/**
 *
 * Handles Fill requests
 *
 * Created by eric on 2/13/19.
 */

public class FillHandler extends Handler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        boolean success = false;

        try {

            System.out.println("Fill handler ran!");

            // only fill request if "POST"
            if (httpExchange.getRequestMethod().toLowerCase().equals("post")) {

                // get number of generations to generate
                String path = httpExchange.getRequestURI().getPath();
                System.out.println(path);
                String[] uriArray = path.split("/");
                int numGenerations = 4;

                try {
                    // attempt to parse the last
                    numGenerations = Integer.parseInt(uriArray[uriArray.length - 1]);
                } catch (Exception e) {
                    // leave it as 4
                }

                // get username
                String username = uriArray[2];

                // print feedback
                System.out.println("Username: " + username);
                System.out.println("The number of generations is " + numGenerations);

                // run register request
                BasicResponse fillResponse = FillService.fill(username, numGenerations);

                // send OK http response key
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                // send response (login response)
                String responseJson = fillResponse.serialize();
                OutputStream responseBody = httpExchange.getResponseBody();
                Handler.writeString(responseJson, responseBody);

                // close http transaction
                responseBody.close();

                success = true;

            }

            if (!success) {
                throw new DataAccessException("Fill failed");
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
