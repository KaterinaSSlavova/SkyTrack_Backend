package skytrack.business.useCase.service;

import com.stripe.exception.StripeException;
import skytrack.dto.booking.CreateBookingRequest;
import skytrack.dto.payment.PaymentRequest;
import skytrack.dto.payment.PaymentResponse;

public interface PaymentService {
    PaymentResponse createPayment(PaymentRequest request) throws StripeException;
    CreateBookingRequest getPendingBooking(String paymentIntentId);
}
