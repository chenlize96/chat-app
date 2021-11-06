package edu.rice.comp504.model.notification;

import org.junit.Test;

import static org.junit.Assert.*;

public class NotificationTest {
    Notification test = SimpleNotification.make("sender", "receiver", "test");

    @Test
    public void getType() {
        assertEquals(test.getType(), "simple");
    }

    @Test
    public void isHasButton() {
        assertEquals(test.isHasButton(), false);
    }

    @Test
    public void isReadStatus() {
        assertEquals(test.isReadStatus(), false);
    }

    @Test
    public void getInfo() {
        assertEquals(test.getInfo(), "test");
    }

    @Test
    public void getReceiver() {
        assertEquals(test.getReceiver(), "receiver");
    }

    @Test
    public void buttonRemove() {
        test.buttonRemove();
        assertEquals(test.isHasButton(), false);
    }

    @Test
    public void notificationRead() {
        test.notificationRead();
        assertEquals(test.isReadStatus(), true);
    }

    @Test
    public void make() {
    }

    @Test
    public void getSender() {
        assertEquals(test.getSender(), "sender");
    }

    @Test
    public void setSender() {
        test.setSender("test2");
        assertEquals(test.getSender(), "test2");
    }

    @Test
    public void getTimestamp() {
        test.setTimestamp("111");
        assertEquals(test.getTimestamp(), "111");
    }

    @Test
    public void setTimestamp() {
        test.setTimestamp("111");
        assertEquals(test.getTimestamp(), "111");
    }
}