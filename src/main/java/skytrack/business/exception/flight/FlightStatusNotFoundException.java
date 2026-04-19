package skytrack.business.exception.flight;

public class FlightStatusNotFoundException extends RuntimeException {
    public FlightStatusNotFoundException(Long id) {
      super("Flight status with id "+ id+ " not found!");
    }
}
