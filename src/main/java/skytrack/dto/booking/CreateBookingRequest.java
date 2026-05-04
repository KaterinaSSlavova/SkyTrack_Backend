package skytrack.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import skytrack.dto.duffel.DuffelFlightResponse;
import skytrack.dto.user.PassengerRequest;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingRequest {
    private PassengerRequest passenger;
    private DuffelFlightResponse flight;
    private Long seatId;
}
