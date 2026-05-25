package skytrack.business.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Service;
import skytrack.business.useCase.service.PaymentService;
import skytrack.dto.booking.CreateBookingRequest;
import skytrack.dto.payment.PaymentRequest;
import skytrack.dto.payment.PaymentResponse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final Map<String, CreateBookingRequest> pendingBooking = new ConcurrentHashMap<>();

    public PaymentResponse createPayment(PaymentRequest request) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(request.getAmount())
                .setCurrency(request.getCurrency() != null ? request.getCurrency() : "eur")
                .addPaymentMethodType("card")
                .build();
        PaymentIntent intent = PaymentIntent.create(params);

        if(request.getBookingRequest() != null){
            pendingBooking.put(intent.getId(), request.getBookingRequest());
        }

        return new PaymentResponse(intent.getClientSecret());
    }

    public CreateBookingRequest getPendingBooking(String paymentIntentId) {
        return pendingBooking.remove(paymentIntentId);
    }
}