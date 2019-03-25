package handlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.nio.file.*;

/**
 *
 * Handles Default/File requests
 *
 * Created by eric on 2/13/19.
 */

public class DefaultHandler extends Handler {

    @Override
    public void handle(HttpExchange httpExchange) {

        try {

            // gets everything past the ip.address:port including '/...'
            String uri = httpExchange.getRequestURI().toString();

            Path filePath = null;

            // handle default request
            if (uri.equals("/")) {
                String filePathStr = "web/index.html";
                String path = Paths.get("").toAbsolutePath().toString() + filePathStr;
                filePath = FileSystems.getDefault().getPath(filePathStr);
            } else {
                // attempt to find the requested file
                filePath = FileSystems.getDefault().getPath("web" + uri);

                // check to see if the file exists:
                String path = filePath.toString();
                System.out.println(path);
                File file = new File(path);

                System.out.println("File " + path + " exists: " + file.exists());

                if (!file.exists()) {
                    String page404 = "web/HTML/404.html";
                    filePath = FileSystems.getDefault().getPath(page404);
                }

            }

            // send response header
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_ACCEPTED, 0);
            OutputStream httpResponseBody = httpExchange.getResponseBody();
            Files.copy(filePath, httpResponseBody);
            httpResponseBody.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
