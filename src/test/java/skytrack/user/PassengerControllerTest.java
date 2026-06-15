package skytrack.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.cache.CacheManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import skytrack.business.service.JwtService;
import skytrack.business.useCase.service.PassengerValidation;
import skytrack.dto.user.PassengerRequest;
import skytrack.presentation.controller.PassengerController;
import skytrack.presentation.security.JwtAuthenticationFilter;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PassengerController.class)
class PassengerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CacheManager cacheManager;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private PassengerValidation passengerValidation;

    @BeforeEach
    void setupFilter() throws Exception {
        doAnswer(invocation -> {
            jakarta.servlet.FilterChain chain = invocation.getArgument(2);
            chain.doFilter(invocation.getArgument(0), invocation.getArgument(1));
            return null;
        }).when(jwtAuthenticationFilter).doFilter(any(), any(), any());
    }

    @Test
    @WithMockUser
    void validatePassenger_shouldReturn204() throws Exception {
        PassengerRequest request = new PassengerRequest();
        request.setFirstName("Jane");
        request.setLastName("Doe");
        request.setEmail("jane@example.com");
        request.setGender("FEMALE");
        request.setNationality("Dutch");
        request.setDateOfBirth(LocalDate.of(1990, 1, 1));
        request.setPassportNumber("AB123456");
        request.setPassportExpiry(LocalDate.of(2030, 1, 1));

        mockMvc.perform(post("/passengers/validate")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .param("departureDate", "2026-07-01"))
                .andExpect(status().isNoContent());

        verify(passengerValidation).validateAge(any());
        verify(passengerValidation).validatePassportNumber(any());
        verify(passengerValidation).validatePassportExpiration(any(), any());
    }

    @Test
    @WithMockUser
    void validatePassenger_missingRequiredFields_shouldReturn400() throws Exception {
        PassengerRequest request = new PassengerRequest();

        mockMvc.perform(post("/passengers/validate")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .param("departureDate", "2026-07-01"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(passengerValidation);
    }
}