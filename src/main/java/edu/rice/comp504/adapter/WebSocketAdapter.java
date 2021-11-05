package edu.rice.comp504.adapter;

import com.google.gson.Gson;
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
import edu.rice.comp504.model.message.TextMessage;
import edu.rice.comp504.model.notification.InviteNotification;
import edu.rice.comp504.model.notification.Notification;
import edu.rice.comp504.model.notification.NotificationFac;
import edu.rice.comp504.model.notification.SimpleNotification;
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

                // check hate speech
                if (body.contains("FUCK") || body.contains("Fuck") || body.contains("fuck")) {
                    Map<String,Integer> hateSpeechCount = UserDB.getHateSpeechCount();
                    hateSpeechCount.put(sender,hateSpeechCount.getOrDefault(sender,0)+1);
                    if(hateSpeechCount.get(sender) == 1) {
                        //send WarningNotification
                        Notification notification = new NotificationFac().make("warn","",sender,room);
                        User hateSpeaker = UserDB.getUsers().get(sender);
                        hateSpeaker.addNotification(notification);
                    } else if(hateSpeechCount.get(sender) == 2) {
                        //mute by all rooms
                        for(ChatRoom chatRoom:RoomDB.make().getRooms().values()) {
                            if(chatRoom.getType().equals("groupchat")) {
                                ((GroupChat)chatRoom).addToMuteList(sender);
                            }
                        }
                    }
                }

                Message messageObj = createMessage(sender, room, body);
                // check if muted or the input parameters are incorrect
                if(messageObj instanceof NullMessage) {
                    break;
                } else { // broadcast to every one in the room
                    MsgToClientSender.broadcastMessage(UserDB.getUser(session), room,  messageObj);
                }
                break;

            case "updateMessage":
                // TODO: update message function here, broadcast message list, History is also here
                String updateRoom = jo.get("roomName").getAsString();
                List<Message> messageList = MessageDB.make().getMessageMap().get(updateRoom);
                MsgToClientSender.updateMessages(session,updateRoom,messageList);
                break;

            case "notification":
                // TODO: update the notifications of a given user
                String username = jo.get("username").getAsString();
                User user = UserDB.getUsers().get(username);
                List<Notification> notificationList = user.getNotificationsList();
                MsgToClientSender.sendNotificationList(username,notificationList);
                break;

            case "invite":
                String inviteTarget = jo.get("userGetInvite").getAsString();
                String inviteSource = jo.get("userSendInvite").getAsString();
                // TODO: invite function here, need notification
                String roomName = jo.get("roomName").getAsString();
                RegisteredUser temp = ((RegisteredUser) UserDB.getUsers().get(inviteTarget));
                NotificationFac fac = new NotificationFac();
                InviteNotification invite = (InviteNotification) fac.make("invite", inviteSource, inviteTarget,
                        inviteSource + "invite you to join" + roomName);
                temp.addNotification(invite);
                MsgToClientSender.sendInviteNotification(roomName,invite,inviteTarget);
                break;

            case "mute":
                String userMuted = jo.get("userMute").getAsString();
                String roomName2 = jo.get("roomName").getAsString();
                ChatRoom chatRoom = RoomDB.getONLY().getRooms().get(roomName2);
                ((GroupChat)chatRoom).addToMuteList(userMuted);
                //broadcast
                MsgToClientSender.broadcastMuteMessage(userMuted,roomName2);
                //send a notification to the person who got muted
                User mutedUser = UserDB.getUsers().get(userMuted);
                mutedUser.addNotification(new NotificationFac().make("mute","",userMuted,roomName2));
                break;

            case "kick":
                String userKicked = jo.get("userKick").getAsString();
                String kickRoomName = jo.get("roomName").getAsString();
                ChatRoom kickChatRoom = RoomDB.getONLY().getRooms().get(kickRoomName);
                User kickedUser = UserDB.getUsers().get(userKicked);
                // kick the user from the room, decrease the number of members, remove the room from user's room list
                ((GroupChat)kickChatRoom).kickUser(userKicked);
                //broadcast
                MsgToClientSender.broadcastKickMessage(userKicked,kickRoomName);
                //send a notification to the person who got kicked
                kickedUser.addNotification(new NotificationFac().make("kick", "",userKicked,kickRoomName));
                break;

            case "block":
                String userBlocked = jo.get("userBlock").getAsString();
                // TODO: block function here, need notification
                break;

            case "getInviteUsers":
                String roomNameCurr = jo.get("roomName").getAsString();
                String userNameCurr = jo.get("userSendInvite").getAsString();
                GroupChat roomCurr2 = ((GroupChat) RoomDB.make().getRooms().get(roomNameCurr));
                List<User> res = new ArrayList<>();
                List<User> userAll = new ArrayList<>();
                for (Map.Entry<String, User> x : (UserDB.getUsers()).entrySet()) {
                    userAll.add(x.getValue());
                }
                List<String> userListName = roomCurr2.getUserList();
                Set<String> roomUserSet = new HashSet<>();
                for (String x : userListName) {
                    roomUserSet.add(x);
                }
                for (int i = 0 ; i < userAll.size() ; i++)
                {
                    if (roomUserSet.contains(userAll.get(i).getUsername())) {
                        continue;
                    }
                    res.add(userAll.get(i));
                }
                MsgToClientSender.sendInviteList(roomNameCurr,res,userNameCurr);
                break;

//            case "acceptInvite":
//                String inviteTarget3 = jo.get("userGetInvite3").getAsString();
//                String inviteSource3 = jo.get("userSendInvite3").getAsString();
//                String roomName3 = jo.get("roomName3").getAsString();
//                RegisteredUser temp3 = ((RegisteredUser) UserDB.getUsers().get(inviteTarget3));
//                NotificationFac fac3 = new NotificationFac();
//                temp.addNotification(fac.make("invite", inviteSource3, inviteTarget3, inviteSource3 + "invite you to join" + roomName3));
//                break;
//
//            case "rejectInvite":
//                String inviteTarget2 = jo.get("userGetInvite2").getAsString();
//                String inviteSource2 = jo.get("userSendInvite2").getAsString();
//                String roomName2 = jo.get("roomName2").getAsString();
//                RegisteredUser temp2 = ((RegisteredUser) UserDB.getUsers().get(inviteTarget2));
//                NotificationFac fac2 = new NotificationFac();
//                temp2.getNotificationsList().(fac2.make("invite", inviteSource2, inviteTarget2, inviteSource2 + "invite you to join" + roomName2));
//                break;
            case "leave":
                String roomLeft = jo.get("roomName").getAsString();
                String userLeft = jo.get("username").getAsString();
                // TODO: leave function here, need notification or message?
                RegisteredUser userLeftCurr = ((RegisteredUser) UserDB.getUsers().get(userLeft));
                ChatRoom roomLeftCurr = RoomDB.make().getRooms().get(roomLeft);
                if (roomLeftCurr.getType().equals("groupchat")) {
                    //groupchat+admin+size != 1 ===cannot leave
                    if (((GroupChat)roomLeftCurr).getOwner().equals(userLeft) && ((GroupChat)roomLeftCurr).getCurNumUser() != 1) {
                        MsgToClientSender.setLeaveResult(roomLeft, false, userLeft);
                    }
                    else {
                        //leave meesage before left to all users in the group
                        MsgToClientSender.broadcastMessage(roomLeftCurr.getRoomName(), roomLeft, TextMessage.make("",
                                ((GroupChat)roomLeftCurr).getOwner() , userLeft+" leave the room by himself", "", "", 12));
                        userLeftCurr.removeAChatRoom(roomLeftCurr);
                        if (((GroupChat)roomLeftCurr).getOwner().equals(userLeft)) {
                            RoomDB.make().getRooms().remove(roomLeft);
                        }
                        else {
                            int currUserNum = ((GroupChat)roomLeftCurr).getCurNumUser() - 1;
                            ((GroupChat) roomLeftCurr).setCurNumUser(currUserNum);
                            ((GroupChat) roomLeftCurr).getUserList().remove(userLeft);
                            ((GroupChat) roomLeftCurr).getMuteList().remove(userLeft);
                            Map<String,List<String>> map = ((GroupChat) roomLeftCurr).getBlockMap();
                            if(map.containsKey(userLeft)) map.remove(userLeft);
                            for (Map.Entry<String, List<String>> entry : map.entrySet()){
                                List<String> list = entry.getValue();
                                list.remove(userLeft);
                            }
                        }
                        MsgToClientSender.setLeaveResult(roomLeft, true, userLeft);
                    }
                }
                else if(roomLeftCurr.getType().equals("userchat")){
                    User user1 = UserDB.getUsers().get(((UserChat)roomLeftCurr).getUser1());
                    User user2 = UserDB.getUsers().get(((UserChat)roomLeftCurr).getUser2());

                    user1.getRoomList().remove(roomLeftCurr);
                    user2.getRoomList().remove(roomLeftCurr);

                    RoomDB.make().getRooms().remove(roomLeft);
                    NotificationFac fac2 = new NotificationFac();

                    SimpleNotification simple = (SimpleNotification) fac2.make("simple", userLeft, user1.getUsername(),
                            userLeft + " leave room " + roomLeft);
                    SimpleNotification simple2 = (SimpleNotification) fac2.make("simple", userLeft, user2.getUsername(),
                            userLeft + " leave room " + roomLeft);

                    user1.addNotification(simple);
                    user2.addNotification(simple2);

                    MsgToClientSender.setLeaveResult(roomLeft, true, userLeft);

                    MsgToClientSender.sendSimpleNotification(simple, user1.getUsername());
                    MsgToClientSender.sendSimpleNotification(simple2, user2.getUsername());
                }
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
        res.add("Owner: " + owner + "(Admin)");
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
