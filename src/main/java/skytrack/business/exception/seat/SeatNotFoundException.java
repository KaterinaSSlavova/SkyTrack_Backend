package skytrack.business.exception.seat;

public class SeatNotFoundException extends RuntimeException {
    public SeatNotFoundException(Long id) {
        super("Seat with id " + id + " not found!");
    }
}
