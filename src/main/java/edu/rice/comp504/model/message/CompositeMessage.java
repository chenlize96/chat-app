package edu.rice.comp504.model.message;

import java.util.ArrayList;

/**
 * A concrete class to represent multiple text and images mixed message.
 */
public class CompositeMessage extends Message{

    private ArrayList<Message> childrenMessage;


    /**
     * A constructor for composite message.
     * @param timestamp The timestamp
     * @param sendUser The message sender's username
     */
    public CompositeMessage(String timestamp, String sendUser) {
        super(timestamp, sendUser, "composite");
        this.childrenMessage = new ArrayList<>();
    }

    /**
     * Add a text message to composite message.
     * @param textMessage The textMessage object
     */
    public void addChildText(TextMessage textMessage) {
        try {
            this.childrenMessage.add(textMessage);
        } catch (NullPointerException npe) {
            System.out.println("NullPointerException occurs, addChildText failed!");
        }
    }

    /**
     * Add child messages based on an input string stream.
     * @param body input string stream
     */
    public void addMultipleChildFromString(String body) {
        String[] strList = body.split(":sep:");
        for (String i : strList) {
            Message tempMsg = NullMessage.make();
            if (i.charAt(0) == '/') {
                tempMsg = ImageMessage.make("auto", this.getSendUser(), i, 1.0);
                this.addChildImage((ImageMessage) tempMsg);
            } else {
                tempMsg = TextMessage.make("auto", this.getSendUser(), body, "default", "black", 12);
                this.addChildText((TextMessage) tempMsg);
            }
        }
    }

    /**
     * Add an image message to composite message.
     * @param imageMessage The imageMessage object
     */
    public void addChildImage(ImageMessage imageMessage) {
        try {
            this.childrenMessage.add(imageMessage);
        } catch (NullPointerException npe) {
            System.out.println("NullPointerException occurs, addChildImage failed!");
        }
    }

}
