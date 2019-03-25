package com.cs240.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

import daos.DataAccessException;
import daos.Database;
import daos.ResourcePack;
import handlers.ClearHandler;
import handlers.EventHandler;
import handlers.FillHandler;
import handlers.LoadHandler;
import handlers.LoginHandler;
import handlers.PersonHandler;
import handlers.RegisterHandler;
import handlers.DefaultHandler;

/**
 * Created by eric on 2/17/19.
 */

public class Server {

    private static final int MAX_WAITING_CONNECTIONS = 12;
    private HttpServer server;

    /** Initializes HTTP Server */
    private void run(String portNumber) {

        System.out.println("Initializing Family Map HTTP Server");

        try {
            server = HttpServer.create(
                    new InetSocketAddress(Integer.parseInt(portNumber)),
                    MAX_WAITING_CONNECTIONS);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // no explanation of what this does
        server.setExecutor(null);

        // link http paths to handlers
        System.out.println("Creating contexts");
        server.createContext("/clear", new ClearHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/fill", new FillHandler()); // "fill/[userName]" or "fill/[userName]/{generations}"
        server.createContext("/user/login", new LoginHandler());
        server.createContext("/user/register", new RegisterHandler());
        server.createContext("/event", new EventHandler());   // "event" or "event/[eventID]"
        server.createContext("/person", new PersonHandler());  // "person" or "person/[personID]"
        server.createContext("/", new DefaultHandler());

        // start the server
        System.out.println("Starting server on port " + portNumber);
        server.start();

    }

    /** First argument is the port number on which to run the server. */
    public static void main(String[] args) {

        try {
            // make sure database is set up
            Database db = new Database();
            db.createTables();
        } catch (DataAccessException e) {
            e.printStackTrace();
            System.exit(1);
        }

        String portNumber = args[0];
        new Server().run(portNumber);

    }

}
