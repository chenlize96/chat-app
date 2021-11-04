package edu.rice.comp504.controller;

import com.google.gson.Gson;
import edu.rice.comp504.adapter.WebSocketAdapter;
import edu.rice.comp504.model.RoomDB;
import edu.rice.comp504.model.UserDB;
import edu.rice.comp504.model.chatroom.ChatRoom;
import edu.rice.comp504.model.chatroom.GroupChat;
import edu.rice.comp504.model.notification.Notification;
import edu.rice.comp504.model.notification.NotificationFac;
import edu.rice.comp504.model.user.NullUser;
import edu.rice.comp504.model.user.User;
import spark.Session;

import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
            User user = webSocketAdapter.logInUser( request.queryMap().value("username"),
                    request.queryMap().value("password"));
            if(!(user instanceof NullUser)) {
                Session session = request.session(); //  create session here
                if(session.isNew()) {
                    session.attribute("username",request.queryMap().value("username"));
                }
            }
            return gson.toJson(user);
        });

        post("/register", (request, response) -> {
            // TODO: check if the username is in UserDB,
            //  if in, then return false,
            //  otherwise, create a User Class in users Map in UserDB using the data,
            //  then return true => register successfully
            if(UserDB.checkUser(request.queryMap().value("username"))) {
                return gson.toJson(false);
            }
            webSocketAdapter.registerUser(request.queryMap().value("username"),
                    request.queryMap().value("school"),
                    Integer.parseInt(request.queryMap().value("age")),
                    request.queryMap().value("interests"),
                    request.queryMap().value("password"));
            return gson.toJson(true);
        });

        get("/room/update", (request, response) -> {
            String username = request.queryMap().value("username");
            User user = UserDB.getUsers().get(username);
            return gson.toJson(user.getRoomList());
        });

        get("/userInfo", (request, response) -> {
            String username = request.queryMap().value("username");
            User user = UserDB.getUsers().get(username);
            return gson.toJson(user);
        });

        /* the above is the endpoint for index.html
         ****************************************************************************************
         * the below is the endpoint for main.html
         */

        post("/join/getRooms", (request, response) -> {
            String username = request.queryMap().value("username");
            User user = UserDB.getUsers().get(username);
            List<ChatRoom> addedRoomList = user.getRoomList();
            List<ChatRoom> allRoomList = new ArrayList<>(RoomDB.make().getRooms().values());
            // res list (allRoomList - addedRoomList)
            List<ChatRoom> res = new ArrayList<>();
            for(ChatRoom chatRoom : allRoomList) {
                if(!addedRoomList.contains(chatRoom) && chatRoom instanceof GroupChat) {
                    res.add(chatRoom);
                }
            }
            return gson.toJson(res);
        });
        post("/chat/getUsers", (request, response) -> {
            System.out.println("username = " + request.queryMap().value("username"));
            //return AllUser except username==username
            /*
             * const UserList
             * */
            return gson.toJson(true);
        });
        post("/join/group", (request, response) -> {
            String username = request.queryMap().value("username");
            String roomName = request.queryMap().value("roomName");
            User user = UserDB.getUsers().get(username);
            GroupChat joinRoom = (GroupChat) RoomDB.make().getRooms().get(roomName);
            // check if the applicant is already a member of this group chat
            // check if userLimit of the room is still available
            if(joinRoom.getUserList().contains(username) || joinRoom.getCurNumUser() >= joinRoom.getUserLimit()) {
                return gson.toJson(false);
            }
            // if public  -> enter
            //    private -> check password and send a notification to the owner of the room
            if(joinRoom.isPublic()) {
                //add into room
                joinRoom.addToUserList(username);
                user.getRoomList().add(joinRoom);
            } else {
                //send an apply notification
                Notification notification = new NotificationFac().make("apply",username,joinRoom.getOwner(),roomName);
                User owner = UserDB.getUsers().get(joinRoom.getOwner());
                owner.addNotification(notification);
            }
            return gson.toJson(true);
        });

        post("/join/notification/accept", (request, response) -> {
            String sender = request.queryMap().value("sender"); // owner of the room
            String receiver = request.queryMap().value("receiver"); // applicant
            String roomName = request.queryMap().value("roomName");
            User sendUser = UserDB.getUsers().get(sender);
            User receiveUser = UserDB.getUsers().get(receiver);
            GroupChat joinRoom = (GroupChat) RoomDB.make().getRooms().get(roomName);
            //add into room
            receiveUser.addAChatRoom(joinRoom);
            joinRoom.addToUserList(receiver);
            //send notification
            Notification notification = new NotificationFac().make("accept",sender,receiver,roomName);
            receiveUser.addNotification(notification);
            //need to remove the ApplyNotification from room owner's notificationDB
            ArrayList<Notification> senderNotificationList = sendUser.getNotificationsList();
            Iterator<Notification> iterator = senderNotificationList.iterator();
            while(iterator.hasNext()) {
                Notification n = iterator.next();
                if(n.getReceiver().equals(sender) && n.getSender().equals(receiver) && n.getInfo().equals(roomName) && n.getType().equals("apply")) {
                    iterator.remove();
                }
            }
            sendUser.setNotificationList(senderNotificationList);
            //return
            return gson.toJson("successfully join the room");
        });

        post("/join/notification/reject", (request, response) -> {
            String sender = request.queryMap().value("sender"); // owner of the room
            String receiver = request.queryMap().value("receiver"); // applicant
            String roomName = request.queryMap().value("roomName");
            User sendUser = UserDB.getUsers().get(sender);
            User receiveUser = UserDB.getUsers().get(receiver);
            //send notification
            Notification notification = new NotificationFac().make("reject",sender,receiver,roomName);
            receiveUser.addNotification(notification);
            //need to remove the ApplyNotification from room owner's notificationDB
            ArrayList<Notification> senderNotificationList = sendUser.getNotificationsList();
            Iterator<Notification> iterator = senderNotificationList.iterator();
            while(iterator.hasNext()) {
                Notification n = iterator.next();
                if(n.getReceiver().equals(sender) && n.getSender().equals(receiver) && n.getInfo().equals(roomName) && n.getType().equals("apply")) {
                    iterator.remove();
                }
            }
            sendUser.setNotificationList(senderNotificationList);
            //return
            return gson.toJson("fail to join the room");
        });

        get("/logout", (request, response) -> {
            // TODO: remove the session related info
            Session session = request.session();
            session.invalidate();
            // TODO: there is no restriction on the type of return value
            System.out.println("username = " + request.queryMap().value("username"));
            return gson.toJson(true);
        });

        get("/room/update", (request, response) -> {
            // TODO: return the room list of the current user, which should be ArrayList<Room>
            System.out.println(request.queryMap().value("username"));
            return gson.toJson("the room list of the current user");
        });

        post("/create/groupchat", (request, response) -> {
            //TODO: Frontend alert when create fail
            //TODO: parameter lose && copy error
            String roomName = request.queryMap().value("roomName");
            int userLimit = Integer.parseInt(request.queryMap().value("maxUser"));
            boolean isPublic = Boolean.parseBoolean(request.queryMap().value("isPublic"));
            String roomPassword = "";
            if (!isPublic) {
                roomPassword = request.queryMap().value("password");
            }
            String interest = request.queryMap().value("interest");
            interest = interest.toLowerCase();
            interest = interest.strip();
            String ownerUsername = request.queryMap().value("username");
            System.out.println(userLimit+" "+roomName+" "+interest+" "+ownerUsername+" "+roomPassword);
            return gson.toJson(webSocketAdapter.createGroupChat(userLimit, roomName, interest, ownerUsername,
                    isPublic, roomPassword));
        });

        post("/sendMessage", (request, response) -> {
            System.out.println("username = " + request.queryMap().value("username") +
                    " roomName = " + request.queryMap().value("roomName") +
                    " message = " + request.queryMap().value("message")
            );
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
