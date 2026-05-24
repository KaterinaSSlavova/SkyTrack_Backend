package skytrack.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class PaymentResponse {
    private String clientSecret;
}
