package skytrack.business.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Service;
import skytrack.dto.payment.PaymentRequest;
import skytrack.dto.payment.PaymentResponse;

@Service
public class PaymentService {

    public PaymentResponse createPayment(PaymentRequest request) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(request.getAmount())
                .setCurrency(request.getCurrency() != null ? request.getCurrency() : "eur").build();
        PaymentIntent intent = PaymentIntent.create(params);
        return new PaymentResponse(intent.getClientSecret());
    }
}