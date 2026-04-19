package skytrack.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import skytrack.presentation.security.JwtAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
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