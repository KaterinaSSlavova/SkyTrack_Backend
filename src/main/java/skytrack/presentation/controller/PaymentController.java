package skytrack.presentation.controller;

import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skytrack.business.service.PaymentService;
import skytrack.dto.payment.PaymentRequest;
import skytrack.dto.payment.PaymentResponse;

@RestController
@RequestMapping("payment")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/create-payment-intent")
    public ResponseEntity<PaymentResponse> createPaymentIntent(
            @RequestBody PaymentRequest request) throws StripeException {
        return ResponseEntity.ok(paymentService.createPayment(request));
    }
}
