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
 * Handles Register requests
 *
 * Created by eric on 2/13/19.
 */

public class RegisterHandler extends Handler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        boolean success = false;

        try {

            System.out.println("Register handler ran!");

            // only fill request if "POST"
            if (httpExchange.getRequestMethod().toLowerCase().equals("post")) {

                System.out.println("method is POST");

                // get request headers
                Headers requestHeaders = httpExchange.getRequestHeaders();

                // get request body as RegisterRequest object
                InputStream requestBody = httpExchange.getRequestBody();
                String jsonInput = Handler.readString(requestBody);
                RegisterRequest registerRequest = null;
                registerRequest = (RegisterRequest) JsonPacket.deserialize(jsonInput, RegisterRequest.class);

                // run register request
                LoginResponse registerResponse = RegisterService.register(registerRequest);

                // fill the user's tree
                String username = registerRequest.getUserName();
                FillService.fill(username, 4);

                // send OK http response key
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                // send response (login response)
                String loginJson = registerResponse.serialize();
                OutputStream responseBody = httpExchange.getResponseBody();
                Handler.writeString(loginJson, responseBody);

                // close http transaction
                responseBody.close();

                // temp
                System.out.println(registerRequest.toString());

            }


            if (!success) {
                throw new DataAccessException("Registration failed.");
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

            e.printStackTrace();

        }

    }
}
