package skytrack.flight;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.cache.CacheManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import skytrack.business.service.JwtService;
import skytrack.business.useCase.flight.*;
import skytrack.dto.duffel.DuffelFlightResponse;
import skytrack.dto.duffel.SavedFlightResponse;
import skytrack.persistence.enumeration.FlightStatus;
import skytrack.presentation.controller.FlightController;
import skytrack.presentation.security.JwtAuthenticationFilter;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FlightController.class)
class FlightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CacheManager cacheManager;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private SearchDuffelFlightUseCase searchDuffelFlightUseCase;

    @MockitoBean
    private SaveDuffelUseCase saveDuffelUseCase;

    @MockitoBean
    private UpdateFlightStatusUseCase updateFlightStatusUseCase;

    @MockitoBean
    private UpdateFlightGateUseCase updateFlightGateUseCase;

    @MockitoBean
    private GetAllDuffelFlightsUseCase getAllDuffelFlightsUseCase;

    @MockitoBean
    private GetDuffelFlightUseCase getDuffelFlightUseCase;

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
    void searchFlights_shouldReturn200() throws Exception {
        DuffelFlightResponse flight = DuffelFlightResponse.builder()
                .externalId("dfl_123")
                .build();

        when(searchDuffelFlightUseCase.searchFlights("AMS", "JFK", LocalDate.of(2026, 7, 1)))
                .thenReturn(reactor.core.publisher.Mono.just(List.of(flight)));

        mockMvc.perform(get("/flights/duffel/search")
                        .param("departureIata", "AMS")
                        .param("arrivalIata", "JFK")
                        .param("departureDate", "2026-07-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].externalId").value("dfl_123"));

        verify(searchDuffelFlightUseCase).searchFlights("AMS", "JFK", LocalDate.of(2026, 7, 1));
    }

    @Test
    @WithMockUser
    void saveFlight_shouldReturn201() throws Exception {
        DuffelFlightResponse request = DuffelFlightResponse.builder()
                .externalId("dfl_123")
                .build();

        SavedFlightResponse response = SavedFlightResponse.builder()
                .id(1L)
                .build();

        when(saveDuffelUseCase.saveFlight(any(DuffelFlightResponse.class))).thenReturn(response);

        mockMvc.perform(post("/flights/duffel/save")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        verify(saveDuffelUseCase).saveFlight(any(DuffelFlightResponse.class));
    }

    @Test
    @WithMockUser
    void getAllDuffelFlights_shouldReturn200() throws Exception {
        SavedFlightResponse flight = SavedFlightResponse.builder()
                .id(1L)
                .build();

        when(getAllDuffelFlightsUseCase.getAllDuffelFlights()).thenReturn(List.of(flight));

        mockMvc.perform(get("/flights/duffel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(getAllDuffelFlightsUseCase).getAllDuffelFlights();
    }

    @Test
    @WithMockUser
    void getAllDuffelFlights_empty_shouldReturn200WithEmptyList() throws Exception {
        when(getAllDuffelFlightsUseCase.getAllDuffelFlights()).thenReturn(List.of());

        mockMvc.perform(get("/flights/duffel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(getAllDuffelFlightsUseCase).getAllDuffelFlights();
    }

    @Test
    @WithMockUser
    void getDuffelFlightById_shouldReturn200() throws Exception {
        SavedFlightResponse response = SavedFlightResponse.builder()
                .id(1L)
                .build();

        when(getDuffelFlightUseCase.getDuffelFlightById(1L)).thenReturn(response);

        mockMvc.perform(get("/flights/duffel/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(getDuffelFlightUseCase).getDuffelFlightById(1L);
    }

    @Test
    @WithMockUser
    void updateFlightGate_shouldReturn204() throws Exception {
        mockMvc.perform(patch("/flights/duffel/1/gate")
                        .param("gate", "B12"))
                .andExpect(status().isNoContent());

        verify(updateFlightGateUseCase).updateFlightGate(1L, "B12");
    }

    @Test
    @WithMockUser
    void updateFlightStatus_shouldReturn204() throws Exception {
        mockMvc.perform(patch("/flights/duffel/1/status")
                        .param("status", "DELAYED"))
                .andExpect(status().isNoContent());

        verify(updateFlightStatusUseCase).updateFlightStatus(eq(1L), eq(FlightStatus.DELAYED), isNull());
    }

    @Test
    @WithMockUser
    void updateFlightStatus_withNewDepTime_shouldReturn204() throws Exception {
        mockMvc.perform(patch("/flights/duffel/1/status")
                        .param("status", "DELAYED")
                        .param("newDepTime", "2026-07-01T14:00:00"))
                .andExpect(status().isNoContent());

        verify(updateFlightStatusUseCase).updateFlightStatus(eq(1L), eq(FlightStatus.DELAYED), any());
    }
}