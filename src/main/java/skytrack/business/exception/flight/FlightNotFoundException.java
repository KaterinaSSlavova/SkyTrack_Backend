package skytrack.business.exception.flight;

public class FlightNotFoundException extends RuntimeException {
    public FlightNotFoundException(Long id) {
      super("Flight with id "+ id + " not found!");
    }
}
