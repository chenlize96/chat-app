package edu.rice.comp504.adapter;

import edu.rice.comp504.model.MessageDB;
import edu.rice.comp504.model.MsgToClientSender;
import edu.rice.comp504.model.RoomDB;
import edu.rice.comp504.model.UserDB;
import edu.rice.comp504.model.chatroom.ChatRoom;
import edu.rice.comp504.model.chatroom.GroupChat;
import edu.rice.comp504.model.user.NullUser;
import edu.rice.comp504.model.user.User;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.util.List;
import java.util.Map;

/**
 * Create a web socket for the server.
 */
@WebSocket
public class WebSocketAdapter {

    /**
     * Open user's session.
     *
     * @param session The user whose session is opened.
     */
    @OnWebSocketConnect
    public void onConnect(Session session) {
        String username = "User" + UserDB.genNextUserId();
        UserDB.addSessionUser(session, username);
    }

    /**
     * Close the user's session.
     *
     * @param session The use whose session is closed.
     */
    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        UserDB.removeUser(session);
    }

    /**
     * Send a message.
     *
     * @param session The session user sending the message.
     * @param message The message to be sent.
     */
    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        MsgToClientSender.broadcastMessage(UserDB.getUser(session), message);
    }

    public void registerUser(String username, String school, int age, String interestsTemp, String password) {
        UserDB.addUser(username, school, age, interestsTemp, password);
    }

    public User logInUser(String username, String password) {
        Map<String, User> usersTemp = UserDB.getUsers();
        if (usersTemp.containsKey(username)) {
            if (usersTemp.get(username).getPassword().equals(password))
                return usersTemp.get(username);
        }
        return new NullUser("", "", "", 0, "");
    }

    /**
     * Create a room, add this room to it's owner's roomList
     * */
    public boolean createGroupChat(int userLimit, String roomName, String interest,
                                String ownerUsername, boolean isPublic, String roomPassword) {
        if (RoomDB.make().addGroupRoom(userLimit, roomName, interest, ownerUsername, isPublic, roomPassword)) {
            Map<String, ChatRoom> rooms = RoomDB.make().getRooms();
            GroupChat room = (GroupChat) rooms.get(roomName);
            User user = UserDB.getUsers().get(ownerUsername);
            user.addAChatRoom(room);
            return true;
        }
        return false;
    }

    /**
     * Adapter's create message function.
     * @param sender sender's username
     * @param room room's name
     * @param body message body text
     * @return true if message was created successfully, false otherwise
     */
    public boolean createMessage(String sender, String room, String body) {
        //MUTE : check if the sender is muted in the given room
        //BTW, BLOCK will be checked in /updateMessage
        ChatRoom chatRoom = RoomDB.make().getRooms().get(room);
        if(chatRoom.getType().equals("groupchat")) { //check mute
            List<String> mutedUsers = ((GroupChat)chatRoom).getMuteList();
            if(mutedUsers.contains(sender)) {
                return false;
            }
        }
        return MessageDB.make().addMessage(sender, room, body, "composite");
    }



}
