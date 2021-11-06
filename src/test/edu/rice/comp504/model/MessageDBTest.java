package edu.rice.comp504.model;

import edu.rice.comp504.model.message.CompositeMessage;
import edu.rice.comp504.model.message.ImageMessage;
import edu.rice.comp504.model.message.TextMessage;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;

public class MessageDBTest {

    @Test
    public void make() {
    }

    @Test
    public void getMessageMap() {
        assertEquals(MessageDB.make().getMessageMap(), new ConcurrentHashMap<>());
    }

    @Test
    public void addMessage() {
        assertEquals(MessageDB.make().addMessage("sender", "room", "body", "text"), TextMessage.make("auto", "sender", "body", "default", "black", 12));
        assertEquals(MessageDB.make().addMessage("sender", "room", "body", "image"), ImageMessage.make("auto", "sender", "body", 1.0));
    }

    @Test
    public void getNextMessageID() {
    }
}