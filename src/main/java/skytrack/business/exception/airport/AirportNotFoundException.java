package skytrack.business.exception.airport;

public class AirportNotFoundException extends RuntimeException {
    public AirportNotFoundException(Long id) {
      super("Airport with id " + id + " not found!");
    }
    public AirportNotFoundException(String iataCode) {
        super("Airport with IATA code " + iataCode + " not found!");
    }
}
