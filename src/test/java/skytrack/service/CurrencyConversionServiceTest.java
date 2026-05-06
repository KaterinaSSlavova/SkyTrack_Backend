package skytrack.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import org.springframework.web.reactive.function.client.WebClient;
import skytrack.business.service.CurrencyConversionService;
import skytrack.dto.frankfurter.FrankfurterResponse;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class CurrencyConversionServiceTest {
    @Mock
    private WebClient frankfurterClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private CurrencyConversionService currencyConversionService;

    @Test
    void convertCurrency_shouldReturnTheSamePrice_whenCurrencyIsEUR(){
        BigDecimal price = new BigDecimal("100.00");

        StepVerifier.create(currencyConversionService.convertToEur(price, "EUR"))
                .expectNext(price).verifyComplete();

        verifyNoInteractions(frankfurterClient);
    }

    @Test
    void convertToEur_shouldReturnTheSamePrice_whenCurrencyIsEurLowercase() {
        BigDecimal price = new BigDecimal("100.00");

        StepVerifier.create(currencyConversionService.convertToEur(price, "eur"))
                .expectNext(price)
                .verifyComplete();

        verifyNoInteractions(frankfurterClient);
    }

    @Test
    void convertCurrency_shouldReturnConvertedPrice_whenCurrencyIsUSD(){
        BigDecimal price = new BigDecimal("100.00");
        BigDecimal convertedPrice = new BigDecimal("92.00");

        FrankfurterResponse response = new FrankfurterResponse();
        response.setRates(Map.of("EUR", convertedPrice));

        when(frankfurterClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(FrankfurterResponse.class)).thenReturn(Mono.just(response));

        StepVerifier.create(currencyConversionService.convertToEur(price, "USD"))
                .expectNext(convertedPrice)
                .verifyComplete();
    }

    @Test
    void convertCurrency_shouldThrowException_whenApiFails() {
        BigDecimal price = new BigDecimal("100.00");

        when(frankfurterClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(FrankfurterResponse.class))
                .thenReturn(Mono.error(new RuntimeException("API unavailable")));

        StepVerifier.create(currencyConversionService.convertToEur(price, "USD"))
                .expectError(RuntimeException.class)
                .verify();
    }
}
