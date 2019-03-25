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
import requests.LoginRequest;
import responses.BasicResponse;
import responses.LoginResponse;
import services.ClearService;
import services.LoginService;

/**
 *
 * Handles Clear requests
 *
 * Created by eric on 2/13/19.
 */

public class ClearHandler extends Handler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        boolean success = false;

        try {

            System.out.println("Clear handler ran!");

            // only fill request if "POST"
            if (httpExchange.getRequestMethod().toLowerCase().equals("post")) {

                // perform clear
                ClearService.clear();

                // send OK http response key
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                // send success response
                OutputStream responseBody = httpExchange.getResponseBody();
                BasicResponse successResponse = new BasicResponse("Clear succeeded.");
                String responseJson = successResponse.serialize();
                Handler.writeString(responseJson, responseBody);
                responseBody.close();

                success = true;

            }

            if (!success) {
                throw new DataAccessException("Clear failed.");
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
