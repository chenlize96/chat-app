package edu.rice.comp504.adapter;

import edu.rice.comp504.model.MsgToClientSender;
import edu.rice.comp504.model.RoomDB;
import edu.rice.comp504.model.UserDB;
import edu.rice.comp504.model.chatroom.ChatRoom;
import edu.rice.comp504.model.chatroom.GroupChat;
import edu.rice.comp504.model.user.NullUser;
import edu.rice.comp504.model.user.RegisteredUser;
import edu.rice.comp504.model.user.User;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.util.*;

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
     * Get users in same rooms.
     * */
    public List<User> getKnownUser(String userName){

        List<User> users = new ArrayList<>();
        Set<String> set = new HashSet<>();
        RegisteredUser user = (RegisteredUser) UserDB.getUsers().get(userName);

        for(ChatRoom room : user.getRoomList()){
            if(room.getType().equals("groupchat")){
                List<String> list = ((GroupChat)room).getUserList();
                for(String tmpUser : list){
                    set.add(tmpUser);
                }
            }
        }

        set.remove(userName);

        for(String str : set ){
            users.add(UserDB.getUsers().get(str));
        }

        return users;
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
}
