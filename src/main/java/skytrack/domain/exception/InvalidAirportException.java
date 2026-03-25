package skytrack.domain.exception;

public class InvalidAirportException extends RuntimeException {
    public static final String INVALID_IATA = "Airport code must be 3 letters!";
    public static final String INVALID_COUNTRY = "Airport country must be defined!";
    public static final String INVALID_CITY = "Airport city must be defined!";
    public static final String INVALID_NAME = "Airport name must be defined!";
    public static final String INVALID_TIMEZONE = "Airport timezone must be defined!";

    public InvalidAirportException(String message) {
        super(message);
    }
}
