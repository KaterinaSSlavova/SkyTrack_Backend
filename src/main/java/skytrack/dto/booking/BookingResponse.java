package skytrack.dto.booking;

import skytrack.dto.duffel.SavedFlightResponse;
import skytrack.dto.seat.SeatResponse;
import skytrack.dto.user.PassengerResponse;

import java.math.BigDecimal;

public class BookingResponse {
    private Long id;
    private PassengerResponse passenger;
    private SeatResponse seat;
    private SavedFlightResponse flight;
    private BigDecimal basePrice;
    private BigDecimal totalPrice;
    private String currency;
    private String bookingReference;
}
