package edu.rice.comp504.controller;

import com.google.gson.Gson;
import edu.rice.comp504.adapter.WebSocketAdapter;

import static spark.Spark.*;

/**
 * The chat app controller communicates with all the clients on the web socket.
 */
public class ChatAppController {
    public static WebSocketAdapter webSocketAdapter = new WebSocketAdapter();
    /**
     * Chat App entry point.
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        port(getHerokuAssignedPort());
        staticFiles.location("/public");
        webSocket("/chatapp", WebSocketAdapter.class);
        Gson gson = new Gson();
        init();

        post("/login", (request, response) -> {
            // TODO: check if username is in UserDB,
            //  and then check if the password is correct,
            //  return true => direct to the main page AND
            //  set the main page as the user instance => see the available rooms
            //  and update user info on the leftmost column, otherwise return false and do nothing

            return gson.toJson(webSocketAdapter.logInUser( request.queryMap().value("username"),
                    request.queryMap().value("password")));
        });

        post("/register", (request, response) -> {
            // TODO: check if the username is in UserDB,
            //  if in, then return false,
            //  otherwise, create a User Class in users Map in UserDB using the data,
            //  then return true => register successfully
            webSocketAdapter.registerUser(request.queryMap().value("username"),
                    request.queryMap().value("school"),
                    Integer.parseInt(request.queryMap().value("age")),
                    request.queryMap().value("interests"),
                    request.queryMap().value("password"));
            return gson.toJson(true);
        });

        /* the above is the endpoint for index.html
         ****************************************************************************************
         * the below is the endpoint for main.html
         */

        get("/logout", (request, response) -> {
            // TODO: there is no restriction on the type of return value
            System.out.println("username = " + request.queryMap().value("username"));
            return gson.toJson(true);
        });



    }

    /**
     * Get the heroku assigned port number.
     * @return The heroku assigned port number
     */
    private static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; // return default port if heroku-port isn't set.
    }
}
