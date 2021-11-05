package edu.rice.comp504.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import edu.rice.comp504.model.message.Message;

import java.util.ArrayList;
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
        UserDB.getSessions().forEach(session -> {
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
        });
    }



}