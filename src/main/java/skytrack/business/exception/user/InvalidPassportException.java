package skytrack.business.exception.user;

public class InvalidPassportException extends RuntimeException {
    public InvalidPassportException(String message) {
        super(message);
    }
}