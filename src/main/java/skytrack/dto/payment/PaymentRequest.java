package skytrack.dto.payment;

import lombok.Getter;
import lombok.Setter;
import skytrack.dto.booking.CreateBookingRequest;

@Getter
@Setter
public class PaymentRequest {
    private long amount;
    private String currency;
    private CreateBookingRequest bookingRequest;
}
