package skytrack.business.exception.seat;

public class SeatNotAvailableException extends RuntimeException {
    public SeatNotAvailableException(String seatNumber) {
        super("Seat number " + seatNumber + " is not available! Please select another seat!");
    }
}
