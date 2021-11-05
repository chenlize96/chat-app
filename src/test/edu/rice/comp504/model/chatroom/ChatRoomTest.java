package edu.rice.comp504.model.chatroom;

import org.junit.Test;

import java.beans.PropertyChangeEvent;

import static org.junit.Assert.*;

public class ChatRoomTest {
    ChatRoom test = new GroupChat(5, 1, "test", "test2", "test3",
   false, "111");
//    @Test
//    public void testAddBallToStore() {
//        int num = ballWorldStoreTest.pcs.getPropertyChangeListeners().length;
//        ballWorldStoreTest.loadBall("circle","");
//        assertEquals(num + 1, ballWorldStoreTest.pcs.getPropertyChangeListeners().length);
//
//    }
    @Test
    public void getRoomId() {
        int testNum = test.getRoomId();
        assertEquals(testNum, 1);
    }

    @Test
    public void getUserLimit() {
        int testNum = test.getUserLimit();
        assertEquals(testNum, 5);
    }

    @Test
    public void setUserLimit() {
    }

    @Test
    public void getRoomName() {
    }

    @Test
    public void setRoomName() {
    }

    @Test
    public void getType() {
    }
}