package skytrack.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import skytrack.presentation.security.JwtAuthenticationFilter;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(request -> {
                    var config = new org.springframework.web.cors.CorsConfiguration();

                    config.setAllowedOrigins(List.of(
                            "http://localhost:5173",
                            "http://145.220.72.90:3000",
                            "http://145.220.72.90"
                    ));

                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setExposedHeaders(List.of("Authorization"));
                    config.setAllowCredentials(true);

                    return config;
                }))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/seats/{id}").hasRole("PASSENGER")
                        .requestMatchers(HttpMethod.GET, "/seats/{seatId}/flight/{flightId}").hasRole("PASSENGER")
                        .requestMatchers(HttpMethod.GET, "/bookings").hasAnyRole("PASSENGER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/bookings/{id}").hasAnyRole("PASSENGER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/bookings").hasRole("PASSENGER")
                        .requestMatchers(HttpMethod.PATCH, "/bookings/{id}/cancel").hasRole("PASSENGER")
                        .requestMatchers(HttpMethod.GET, "/bookings/{reference}/qr").hasAnyRole("PASSENGER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/flights/duffel/save").hasRole("PASSENGER")
                        .requestMatchers(HttpMethod.GET, "/flights/duffel/search").hasRole("PASSENGER")
                        .requestMatchers(HttpMethod.GET, "/airports/search").hasRole("PASSENGER")
                        .requestMatchers(HttpMethod.GET, "/flights/search").hasRole("PASSENGER")
                        .requestMatchers(HttpMethod.GET, "/airports/{id}").hasAnyRole("PASSENGER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/airports").hasAnyRole("PASSENGER","ADMIN")
                        .requestMatchers(HttpMethod.GET, "/flights/{id}").hasAnyRole("PASSENGER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/flights").hasAnyRole("PASSENGER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/airports").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/airports/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/airports/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/flights").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/flights/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/flights/{id}/cancel").hasRole("ADMIN")
                        .requestMatchers("/users/me").authenticated()
                        .anyRequest().authenticated()
                ).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}