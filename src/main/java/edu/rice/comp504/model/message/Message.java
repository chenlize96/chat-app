package edu.rice.comp504.model.message;

/**
 * An abstract class to represent all messages object.
 */
public abstract class Message implements IMessageFac{

    private final String timestamp;
    private final String sendUser;
    private final String type;

    /**
     * A constructor for Message.
     * @param timestamp The timestamp
     * @param sendUser The message sender's display name
     */
    public Message(String timestamp, String sendUser, String type) {
        this.timestamp = timestamp;
        this.sendUser = sendUser;
        this.type = type;
    }

    /**
     * Get send user's display name.
     * @return The send user's display name
     */
    public String getSendUser() {
        return sendUser;
    }

    /**
     * Get timestamp of this message.
     * @return The timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Get type of this message.
     * @return The type
     */
    public String getType() {
        return type;
    }

    public static Message make() {
        return NullMessage.make();
    }

}
