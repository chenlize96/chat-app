package edu.rice.comp504.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import edu.rice.comp504.model.chatroom.ChatRoom;

import edu.rice.comp504.model.chatroom.GroupChat;
import edu.rice.comp504.model.chatroom.UserChat;
import edu.rice.comp504.model.message.Message;
import org.eclipse.jetty.websocket.api.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static j2html.TagCreator.p;

/**
 * Send messages to the client.
 */
public class MsgToClientSender {

    /**
     * Broadcast message to all users.
     * @param sender  The message sender.
     * @param message The message.
     */
    public static void broadcastMessage(String sender, String room, Message message) {
        if (message.getType().equals("null")) {
            return;
        }
        ChatRoom chatRoom = RoomDB.make().getRooms().get(room);
        if (chatRoom.getType().equals("groupchat")) {
            //check if the user is in the given room
            List<String> userList = ((GroupChat)chatRoom).getUserList();
            UserDB.getSessions().forEach(session -> {
                String currUser = UserDB.getUserBySession(session);
                if(!userList.contains(currUser)) {
                    return;
                }
                sendJsonObject(room, message, sender, session);
            });
        } else { // userchat
            UserDB.getSessions().forEach(session -> {
                String currUser = UserDB.getUserBySession(session);
                if(!currUser.equals(((UserChat)chatRoom).getUser1()) && !currUser.equals(((UserChat)chatRoom).getUser2())) {
                    return;
                }
                sendJsonObject(room, message, sender, session);
            });
        }
    }

    private static void sendJsonObject(String room, Message message, String sender, Session session) {
        try {
            JsonObject jo = new JsonObject();
            jo.addProperty("username", p(sender).render());
            jo.addProperty("message", new Gson().toJsonTree(message).toString());
            jo.addProperty("room", p(room).render());
            session.getRemote().sendString(String.valueOf(jo));
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*UserDB.getSessions().forEach(session -> {
            String curUser = UserDB.getUserBySession(session);
            try {
                JsonObject jo = new JsonObject();
                // TODO: use .addProperty(key, value) add a JSON object property that has a key "userMessage"
                //  and a j2html paragraph value

                jo.addProperty("message", new Gson().toJsonTree(message).toString());
                jo.addProperty("room", p(room).render());

                session.getRemote().sendString(String.valueOf(jo));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });*/
    }

}
