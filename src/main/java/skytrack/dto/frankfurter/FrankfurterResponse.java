package skytrack.dto.frankfurter;

import lombok.*;

import java.math.BigDecimal;
import java.util.Map;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FrankfurterResponse {
    private Map<String, BigDecimal> rates;
}
