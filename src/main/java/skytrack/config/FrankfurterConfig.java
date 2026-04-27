package skytrack.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class FrankfurterConfig {

    @Value("${frankfurter.base_url}")
    private String baseURl;

    @Bean
    public WebClient frankfurterClient() {
        return WebClient.builder()
                .baseUrl(baseURl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
    }
}
