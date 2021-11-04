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
    public Notification make(String type, String sender, String receiver, String info) {
        switch (type) {
            case "apply":
                return ApplyNotification.make(sender, receiver, info);
            case "request":
                return InteractNotification.make(sender, receiver, info);
            case "kick":
                return SimpleNotification.make(sender, receiver, info);
            case "reject":
                return RejectNotification.make(sender, receiver, info);
            case "accept":
                return AcceptNotification.make(sender, receiver, info);
            default:
                return NullNotification.make();
        }
    }
}
