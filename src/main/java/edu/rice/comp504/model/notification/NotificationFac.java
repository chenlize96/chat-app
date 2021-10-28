package edu.rice.comp504.model.notification;

/**
 * A factory class to make different types of notification.
 */
public class NotificationFac implements INotificationFac{

    /**
     * A constructor.
     */
    public NotificationFac() {

    }


    /**
     * Make function.
     */
    @Override
    public Notification make(String type, String receiver, String info) {
        switch (type) {
            case "invite":

            case "request":
                return InteractNotification.make(receiver, info);

            case "kick":
                return SimpleNotification.make(receiver, info);

            default:
                return NullNotification.make();
        }
    }
}
