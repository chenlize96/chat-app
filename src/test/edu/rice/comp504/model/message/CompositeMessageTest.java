package edu.rice.comp504.model.message;

import org.junit.Test;

import static org.junit.Assert.*;

public class CompositeMessageTest {

    CompositeMessage compositeMessage = new CompositeMessage("","aaa");

    @Test
    public void addChildText() {
        TextMessage textMessage = (TextMessage) TextMessage.make("","aaa",
                "bbb","ccc","ddd",12);
        compositeMessage.addChildText(textMessage);
        assertEquals(textMessage,compositeMessage.getChildrenMessageArrayList().get(0));
    }

    @Test
    public void addMultipleChildFromString() {

    }

    @Test
    public void addChildImage() {
    }

    @Test
    public void getChildrenMessageArrayList() {
    }

    @Test
    public void getChildrenContentAsString() {
    }
}