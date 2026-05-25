package skytrack.presentation.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skytrack.business.useCase.booking.CreateBookingUseCase;
import skytrack.business.useCase.service.PaymentService;
import skytrack.dto.booking.BookingResponse;
import skytrack.dto.booking.CreateBookingRequest;
import skytrack.dto.payment.PaymentRequest;
import skytrack.dto.payment.PaymentResponse;

@RestController
@RequestMapping("payment")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final CreateBookingUseCase createBookingUseCase;

    @PostMapping("/create-payment-intent")
    public ResponseEntity<PaymentResponse> createPaymentIntent(
            @RequestBody PaymentRequest request) throws StripeException {
        return ResponseEntity.ok(paymentService.createPayment(request));
    }

    @PostMapping("/confirm")
    public ResponseEntity<BookingResponse> confirmPayment(@RequestParam String paymentIntentId)
            throws StripeException {
        PaymentIntent intent = PaymentIntent.retrieve(paymentIntentId);

        if(!intent.getStatus().equals("succeeded")){
            return  ResponseEntity.badRequest().build();
        }

        CreateBookingRequest bookingRequest = paymentService.getPendingBooking(intent.getId());
        if(bookingRequest == null){
            return  ResponseEntity.notFound().build();
        }

        BookingResponse booking = createBookingUseCase.toResponse(bookingRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }
}
