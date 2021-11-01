package edu.rice.comp504.controller;

import com.google.gson.Gson;
import edu.rice.comp504.adapter.WebSocketAdapter;

import static spark.Spark.*;

/**
 * The chat app controller communicates with all the clients on the web socket.
 */
public class ChatAppController {

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
            System.out.println("username = " + request.queryMap().value("username") +
                    " password = " + request.queryMap().value("password"));
            return gson.toJson(true);
        });

        post("/register", (request, response) -> {
            // TODO: check if the username is in UserDB,
            //  if in, then return false,
            //  otherwise, create a User Class in users Map in UserDB using the data,
            //  then return true => register successfully
            System.out.println("username = " + request.queryMap().value("username") +
                    " school = " + request.queryMap().value("school") +
                    " age = " + request.queryMap().value("age") +
                    " interests = " + request.queryMap().value("interests") +
                    " password = " + request.queryMap().value("password")
            );
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

        post("/create/groupchat", (request, response) -> {
            // TODO: check if roomName (**unique**) is in UserDB =>
            //  false => create the room (set owner), and return the new roomList of current user,
            //  otherwise return the roomList of current user
            //  (front-end will use the number of the roomList to judge if a room is created)
            //  the return value is used to update rooms
            //  the following data are the minimum requirement for hw6
            System.out.println("username = " + request.queryMap().value("username") +
                    " roomName = " + request.queryMap().value("roomName") +
                    " interest = " + request.queryMap().value("interest") +
                    " maxUser = " + request.queryMap().value("maxUser") +
                    " isPublic = " + request.queryMap().value("isPublic") +
                    " password = " + request.queryMap().value("password")
            );//type of isPublic: String
            //true: close the page, false: no changes
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
