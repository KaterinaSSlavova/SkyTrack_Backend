package skytrack.business.exception.user;

public class UserEmailInvalidException extends RuntimeException {
    public UserEmailInvalidException(String email) {
        super("User with email " + email + " was not found!");
    }
}
