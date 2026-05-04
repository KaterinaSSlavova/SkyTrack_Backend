package skytrack.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import skytrack.dto.frankfurter.FrankfurterResponse;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CurrencyConversionService {
    private final WebClient frankfurterClient;

    public Mono<BigDecimal> convertToEur(BigDecimal price, String currency){
        if(currency.equalsIgnoreCase("EUR")){
            return Mono.just(price);
        }

        return frankfurterClient.get()
                .uri("/latest?from=" + currency +"&to=EUR&amount=" + price)
                .retrieve().bodyToMono(FrankfurterResponse.class)
                .map(r -> r.getRates().get("EUR"));
    }
}