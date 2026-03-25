package skytrack.business.exception;

public class FlightStatusNotFoundException extends RuntimeException {
    public FlightStatusNotFoundException(Long id) {
      super("Flight status with id "+ id+ " not found!");
    }
}
