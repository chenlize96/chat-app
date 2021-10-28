package edu.rice.comp504.model.notification;

/**
 * A concrete class to represent all those notifications which can have interaction buttons.
 */
public class InteractNotification extends Notification{


    /**
     * A constructor.
     * @param receiver Receive user's username
     * @param info The notification info
     */
    private InteractNotification(String receiver, String info) {
        super(receiver, info, "interact", true);
    }

    /**
     * Make function.
     * @param receiver Receive user's username
     * @param info The notification info
     * @return A invite notification
     */
    public static Notification make(String receiver, String info) {
        return new InteractNotification(receiver, info);
    }

}
