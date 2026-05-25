package skytrack.business.exception.user;

public class RefreshTokenException extends RuntimeException {
    public RefreshTokenException() {
        super("Refresh token expired! Please log in again!");
    }
}
