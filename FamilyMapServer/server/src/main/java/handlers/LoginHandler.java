package handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.sql.Connection;

import javax.xml.crypto.Data;

import daos.DataAccessException;
import requests.JsonPacket;
import requests.LoginRequest;
import requests.RegisterRequest;
import responses.BasicResponse;
import responses.LoginResponse;
import services.LoginService;
import services.RegisterService;

/**
 *
 * Handles Login requests
 *
 * Created by eric on 2/13/19.
 */

public class LoginHandler extends Handler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        boolean success = false;

        try {

            System.out.println("Login handler ran!");

            // only fill request if "POST"
            if (httpExchange.getRequestMethod().toLowerCase().equals("post")) {

                // get request headers
                Headers requestHeaders = httpExchange.getRequestHeaders();

                // get request body as LoginRequest object
                InputStream requestBody = httpExchange.getRequestBody();
                String jsonInput = Handler.readString(requestBody);
                LoginRequest loginRequest = null;
                loginRequest = (LoginRequest) JsonPacket.deserialize(jsonInput, LoginRequest.class);

                // run register request
                LoginResponse loginResponse = LoginService.login(loginRequest);

                // send OK http response key
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                // send response (login response)
                String loginJson = loginResponse.serialize();
                OutputStream responseBody = httpExchange.getResponseBody();
                Handler.writeString(loginJson, responseBody);

                // close http transaction
                responseBody.close();

                // temp
                System.out.println(loginRequest.toString());

                success = true;

            }

            if (!success) {
                throw new DataAccessException("Login failed.");
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
