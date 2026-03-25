package skytrack.domain.exception;

public class InvalidFlightException extends RuntimeException{
    public static final String FLIGHT_NUM_NULL = "Flight number cannot be null or blank!";
    public static final String SAME_AIRPORT = "Departure and arrival airport cannot be the same!";
    public static final String INVALID_CAPACITY = "Capacity must be greater than 0!";
    public static final String INVALID_SCHEDULE = "Arrival time must be after departure time!";
    public static final String SCHEDULE_UNDEFINED = "Flight schedule must be defined!";
    public static final String INVALID_PRICE = "Flight price must be greater than 0!";
    public static final String AIRPORT_NULL = "Airport cannot be null!";
    public static final String STATUS_NULL = "Status cannot be null!";

    public InvalidFlightException(String message) {
        super(message);
    }
}
