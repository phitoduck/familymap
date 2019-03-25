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
import requests.LoadRequest;
import requests.RegisterRequest;
import responses.BasicResponse;
import responses.LoginResponse;
import services.LoadService;
import services.RegisterService;

/**
 *
 * Handles Load requests
 *
 * Created by eric on 2/13/19.
 */

public class LoadHandler extends Handler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        boolean success = false;

        try {

            System.out.println("Load handler ran!");

            // only fill request if "POST"
            if (httpExchange.getRequestMethod().toLowerCase().equals("post")) {

                System.out.println("method is POST");

                // get request headers
                Headers requestHeaders = httpExchange.getRequestHeaders();

                // get request body as LoadRequest object
                InputStream requestBody = httpExchange.getRequestBody();
                String jsonInput = Handler.readString(requestBody);
                LoadRequest loadRequest = null;
                loadRequest = (LoadRequest) JsonPacket.deserialize(jsonInput, LoadRequest.class);

                // run register request
                BasicResponse loadResponse = LoadService.load(loadRequest);

                // send OK http response key
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                // send response (login response)
                String responseJson = loadResponse.serialize();
                OutputStream responseBody = httpExchange.getResponseBody();
                Handler.writeString(responseJson, responseBody);

                // close http transaction
                responseBody.close();

            }

            if (!success) {
                throw new DataAccessException("Load failed.");
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
