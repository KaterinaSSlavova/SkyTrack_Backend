package skytrack.business.exception.user;

public class UserTooOldException extends RuntimeException {
    public UserTooOldException() {
        super("Age cannot exceed 120 years!");
    }
}
