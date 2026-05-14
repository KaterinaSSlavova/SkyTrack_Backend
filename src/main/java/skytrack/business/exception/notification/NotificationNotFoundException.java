package skytrack.business.exception.notification;

public class NotificationNotFoundException extends RuntimeException {
    public NotificationNotFoundException(Long id) {
        super("Notification with id " + id + " not found!");
    }
}
