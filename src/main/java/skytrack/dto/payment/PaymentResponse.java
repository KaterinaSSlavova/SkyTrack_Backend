package skytrack.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
public class PaymentResponse {
    private String clientSecret;
    private BigDecimal totalPrice;
}
