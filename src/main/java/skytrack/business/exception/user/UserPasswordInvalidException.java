package skytrack.business.exception.user;

public class UserPasswordInvalidException extends RuntimeException {
    public UserPasswordInvalidException() {
        super("Invalid password!");
    }
}
