package skytrack.business.exception.user;

public class TooManyRequestsException extends RuntimeException {
    public TooManyRequestsException() {
        super("Too many login attempts. Please try again later.");
    }
}
