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
        compositeMessage.addMultipleChildFromString("badTest");
        compositeMessage.addMultipleChildFromString("/:sep:");
        compositeMessage.addMultipleChildFromString("a:sep:");
    }

    @Test
    public void addChildImage() {
    }

    @Test
    public void getChildrenMessageArrayList() {
    }

    @Test
    public void getChildrenContentAsString() {
        TextMessage textMessage = (TextMessage) TextMessage.make("","aaa",
                "bbb","ccc","ddd",12);
        compositeMessage.addChildText(textMessage);
        
//        public String getChildrenContentAsString() {
//            StringBuilder result = new StringBuilder();
//            for (Message m : this.childrenMessage) {
//                switch (m.getType()) {
//                    case "text":
//                        result.append(((TextMessage) m).getBody());
//                        result.append(":sep:");
//                        break;
//
//                    case "image":
//                        result.append(((ImageMessage) m).getSourceUrl());
//                        result.append(":sep:");
//                        break;
//
//                    default:
//                        break;
//                }
//            }
//            return result.toString();
//        }

    }
}