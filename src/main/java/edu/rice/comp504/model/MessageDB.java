package edu.rice.comp504.model;

import edu.rice.comp504.model.chatroom.ChatRoom;
import edu.rice.comp504.model.message.Message;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageDB {

    private static MessageDB ONLY;
    public int nextMessageID = 1;
    private final Map<String, Message> messageMap = new ConcurrentHashMap<>();

    /**
     * A constructor.
     */
    private MessageDB() {

    }

    /**
     * Make function for singleton pattern.
     * @return Static MessageDB object
     */
    public static MessageDB make() {
        if (ONLY == null) {
            ONLY = new MessageDB();
        }
        return ONLY;
    }

    /**
     * Get next message IDs.
     * @return nextMessageID
     */
    public int getNextMessageID() {
        return this.nextMessageID ++;
    }


}
