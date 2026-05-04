package skytrack.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import skytrack.dto.duffel.SavedFlightResponse;
import skytrack.dto.seat.SeatResponse;
import skytrack.dto.user.PassengerResponse;

import java.math.BigDecimal;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
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
