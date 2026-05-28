package skytrack.business.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.useCase.service.PaymentService;
import skytrack.dto.booking.CreateBookingRequest;
import skytrack.dto.payment.PaymentRequest;
import skytrack.dto.payment.PaymentResponse;
import skytrack.persistence.entity.DuffelFlightEntity;
import skytrack.persistence.entity.SeatEntity;
import skytrack.persistence.repo.DuffelRepository;
import skytrack.persistence.repo.SeatRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private Map<String, CreateBookingRequest> pendingBooking = new ConcurrentHashMap<>();
    private final PriceCalculationService priceCalculationService;
    private final SeatRepository seatRepository;
    private final TimeConverter timeConverter;
    private final DuffelRepository duffelRepository;

    public PaymentResponse createPayment(PaymentRequest request) throws StripeException {
        CreateBookingRequest bookingRequest = request.getBookingRequest();
        Instant departureTime = timeConverter.convertToUTC(
                bookingRequest.getFlight().getDepartureLocalTime(),
                bookingRequest.getFlight().getDepartureTimezone()
        );

        DuffelFlightEntity flight = duffelRepository.findByFlightNumberAndDepartureTime
                        (bookingRequest.getFlight().getFlightNumber(), departureTime)
                .orElseThrow(() -> new RuntimeException("Flight not found"));

        SeatEntity seat = seatRepository.findById(bookingRequest.getSeatId())
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        BigDecimal totalPrice = priceCalculationService.calculate(seat, flight.getPrice());
        long totalPriceInCents = totalPrice.multiply(BigDecimal.valueOf(100))
                .setScale(0, RoundingMode.HALF_UP).longValue();

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(totalPriceInCents)
                .setCurrency(request.getCurrency() != null ? request.getCurrency() : "eur")
                .addPaymentMethodType("card")
                .build();
        PaymentIntent intent = PaymentIntent.create(params);

        if(request.getBookingRequest() != null){
            pendingBooking.put(intent.getId(), request.getBookingRequest());
        }

        return new PaymentResponse(intent.getClientSecret(), totalPrice);
    }

    public CreateBookingRequest getPendingBooking(String paymentIntentId) {
        return pendingBooking.remove(paymentIntentId);
    }
}