package skytrack.seat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.cache.CacheManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import skytrack.business.service.JwtService;
import skytrack.business.useCase.seat.GetSeatMapUseCase;
import skytrack.business.useCase.seat.GetSeatUseCase;
import skytrack.dto.seat.SeatMapResponse;
import skytrack.dto.seat.SeatResponse;
import skytrack.presentation.controller.SeatController;
import skytrack.presentation.security.JwtAuthenticationFilter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SeatController.class)
class SeatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CacheManager cacheManager;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private GetSeatUseCase getSeatUseCase;

    @MockitoBean
    private GetSeatMapUseCase getSeatMapUseCase;

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
    void getSeat_shouldReturn200() throws Exception {
        SeatResponse response = SeatResponse.builder()
                .id(1L)
                .build();

        when(getSeatUseCase.getSeat(1L, 2L)).thenReturn(response);

        mockMvc.perform(get("/seats/1/flight/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(getSeatUseCase).getSeat(1L, 2L);
    }

    @Test
    @WithMockUser
    void getSeatMap_shouldReturn200() throws Exception {
        SeatMapResponse response = SeatMapResponse.builder()
                .flightId(1L)
                .build();

        when(getSeatMapUseCase.getSeatMap(1L)).thenReturn(response);

        mockMvc.perform(get("/seats/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flightId").value(1));

        verify(getSeatMapUseCase).getSeatMap(1L);
    }
}