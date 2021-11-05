package edu.rice.comp504.adapter;

import com.google.gson.JsonParser;
import edu.rice.comp504.model.MessageDB;
import edu.rice.comp504.model.MsgToClientSender;
import edu.rice.comp504.model.RoomDB;
import edu.rice.comp504.model.UserDB;
import edu.rice.comp504.model.chatroom.ChatRoom;
import edu.rice.comp504.model.chatroom.GroupChat;
import edu.rice.comp504.model.chatroom.UserChat;
import edu.rice.comp504.model.message.Message;
import edu.rice.comp504.model.message.NullMessage;
import edu.rice.comp504.model.user.NullUser;
import edu.rice.comp504.model.user.RegisteredUser;
import edu.rice.comp504.model.user.User;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import com.google.gson.JsonObject;

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
        //String username = "User" + UserDB.genNextUserId();
        //UserDB.addSessionUser(session, username);
    }

    /**
     * Close the user's session.
     *
     * @param session The use whose session is closed.
     */
    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {

        //UserDB.removeUser(session);
    }

    /**
     * Send a message.
     *
     * @param session The session user sending the message.
     * @param message The message to be sent.
     */
    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        System.out.println(message);
        JsonParser parser = new JsonParser();
        JsonObject jo = (JsonObject) parser.parse(message);
        //System.out.println(jo.get("username").getAsString());
        //MsgToClientSender.broadcastMessage(UserDB.getUser(session), message);

        switch (jo.get("action").getAsString()) {
            case "login":
                mapSessionUser(session, jo.get("username").getAsString());
                break;

            case "send":
                String sender = jo.get("username").getAsString();
                String room = jo.get("roomName").getAsString();
                String body = jo.get("message").getAsString();

                System.out.println(sender+" "+room+" "+body);

                Message messageObj = createMessage(sender, room, body);
                // check if muted or the input parameters are incorrect
                if(messageObj instanceof NullMessage) {
                    break;
                } else { // broadcast to every one in the room
                    MsgToClientSender.broadcastMessage(UserDB.getUser(session), room,  messageObj);
                }
                break;

            case "updatemessage":
                // TODO: update message function here, broadcast message list, History is also here
                String updateroom = jo.get("roomName").getAsString();
                break;

            case "invite":
                String inviteTarget = jo.get("userGetInvite").getAsString();
                String inviteSource = jo.get("userSendInvite").getAsString();
                // TODO: invite function here, need notification
                break;

            case "mute":
                String userMuted = jo.get("userMute").getAsString();
                // TODO: mute function here, need notification??
                break;

            case "kick":
                String userKicked = jo.get("userKick").getAsString();
                // TODO: kick function here, need notification
                break;

            case "block":
                String userBlocked = jo.get("userBlock").getAsString();
                // TODO: block function here, need notification
                break;

            case "leave":
                String roomleft = jo.get("roomName").getAsString();
                String userLeft = jo.get("username").getAsString();
                // TODO: leave function here, need notification or message?
                break;

            default:
                System.out.println("Not valid action!");
                break;
        }

    }

    /**
     * Map websocket session with current username when logging in.
     * @param session websocket session
     * @param username username
     */
    public void mapSessionUser(Session session, String username) {
        UserDB.addSessionUser(session, username);
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
     * Get room list of a user.
     * */
    public List<ChatRoom> getUserRoomList(String userName){
        return (UserDB.getUsers().get(userName)).getRoomList();
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
                    if (RoomDB.make().getRooms().containsKey(tmpUser+","+userName)
                            || RoomDB.make().getRooms().containsKey(userName+","+tmpUser))
                        continue;
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

    /**
     * Create a user chat, add this room to it's owner's roomList
     * */
    public boolean createUserChat(String userName, String friendName) {

        if (RoomDB.make().addUserChat(userName,friendName)) {
            Map<String, ChatRoom> rooms = RoomDB.make().getRooms();
            UserChat room = (UserChat) rooms.get(userName+","+friendName);
            User user = UserDB.getUsers().get(userName);
            User friend = UserDB.getUsers().get(friendName);
            user.addAChatRoom(room);
            friend.addAChatRoom(room);
            return true;
        }
        return false;
    }

    /**
     * Return the list of user/admin/owner of the chosen room
     * */
    public List<String> showAllUsersInside(String roomname) {
        List <String> res = new ArrayList<>();
        if (RoomDB.make().getRooms().get(roomname).getType().equals("userchat"))
        {
            res.add(((UserChat)RoomDB.make().getRooms().get(roomname)).getUser1());
            res.add(((UserChat)RoomDB.make().getRooms().get(roomname)).getUser2());
            return res;
        }
        Set<String> userSet = new HashSet<>();
        List<String> users = ((GroupChat)RoomDB.make().getRooms().get(roomname)).getUserList();
        String owner = ((GroupChat)RoomDB.make().getRooms().get(roomname)).getOwner();
        List<String> admin = ((GroupChat)RoomDB.make().getRooms().get(roomname)).getAdminList();
        for (String x : users)
            userSet.add(x);
//        List <String> res = new ArrayList<>();
        res.add("Owner: " + owner);
        userSet.remove(owner);
        for (String x : admin) {
            res.add("Admin: " + x);
            userSet.remove(x);
        }
        for (String x : userSet) {
            res.add(x);
        }
        return res;
    }

    /**
     * Adapter's create message function.
     * @param sender sender's username
     * @param room room's name
     * @param body message body text
     * @return a message
     */
    public Message createMessage(String sender, String room, String body) {
        //MUTE : check if the sender is muted in the given room
        //BTW, BLOCK will be checked in /updateMessage
        ChatRoom chatRoom = RoomDB.make().getRooms().get(room);
        if(chatRoom.getType().equals("groupchat")) { //check mute
            List<String> mutedUsers = ((GroupChat)chatRoom).getMuteList();
            if(mutedUsers.contains(sender)) {
                return NullMessage.make();
               // return MessageDB.make().addMessage(sender, room, body, "null");

            }
        }
        return MessageDB.make().addMessage(sender, room, body, "composite");
    }



}
