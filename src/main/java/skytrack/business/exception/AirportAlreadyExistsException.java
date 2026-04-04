package skytrack.business.exception;

public class AirportAlreadyExistsException extends RuntimeException {
    public AirportAlreadyExistsException(String iataCode) {
        super("Airport with the IATA code " + iataCode + " already exists!");
    }
}
