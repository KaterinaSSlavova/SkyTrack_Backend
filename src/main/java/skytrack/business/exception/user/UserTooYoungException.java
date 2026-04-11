package skytrack.business.exception.user;

public class UserTooYoungException extends RuntimeException {
    public UserTooYoungException() {
        super("You must be at least 16 years old to have an account.");
    }
}
