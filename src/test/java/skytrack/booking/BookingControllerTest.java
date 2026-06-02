package skytrack.booking;

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
import skytrack.business.service.QrGenerator;
import skytrack.business.useCase.booking.*;
import skytrack.business.useCase.statistics.GetNextFlightUseCase;
import skytrack.business.useCase.statistics.GetStatisticsUseCase;
import skytrack.dto.booking.BookingResponse;
import skytrack.dto.booking.BookingStatsResponse;
import skytrack.dto.booking.CreateBookingRequest;
import skytrack.presentation.controller.BookingController;
import skytrack.presentation.security.JwtAuthenticationFilter;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

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
    private QrGenerator qrGenerator;

    @MockitoBean
    private CreateBookingUseCase createBookingUseCase;

    @MockitoBean
    private GetAllBookingsUseCase getAllBookingsUseCase;

    @MockitoBean
    private GetBookingUseCase getBookingUseCase;

    @MockitoBean
    private CancelBookingUseCase cancelBookingUseCase;

    @MockitoBean
    private GetBookingByReferenceUseCase getBookingByReferenceUseCase;

    @MockitoBean
    private GetStatisticsUseCase getStatisticsUseCase;

    @MockitoBean
    private GetNextFlightUseCase getNextFlightUseCase;

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
    void getQRCode_shouldReturn200() throws Exception {
        byte[] qrBytes = new byte[]{1, 2, 3};
        when(qrGenerator.generate("ABC123")).thenReturn(qrBytes);

        mockMvc.perform(get("/bookings/ABC123/qr"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/png"));

        verify(qrGenerator).generate("ABC123");
    }

    @Test
    @WithMockUser
    void verifyBooking_shouldReturn200() throws Exception {
        BookingResponse response = BookingResponse.builder()
                .id(1L)
                .bookingReference("ABC123")
                .build();

        when(getBookingByReferenceUseCase.getBookingByReference("ABC123")).thenReturn(response);

        mockMvc.perform(get("/bookings/verify/ABC123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.bookingReference").value("ABC123"));

        verify(getBookingByReferenceUseCase).getBookingByReference("ABC123");
    }

    @Test
    @WithMockUser
    void getAllBookings_shouldReturn200() throws Exception {
        BookingResponse booking = BookingResponse.builder()
                .id(1L)
                .bookingReference("ABC123")
                .build();

        when(getAllBookingsUseCase.getAllBookings()).thenReturn(List.of(booking));

        mockMvc.perform(get("/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].bookingReference").value("ABC123"));

        verify(getAllBookingsUseCase).getAllBookings();
    }

    @Test
    @WithMockUser
    void getAllBookings_empty_shouldReturn200WithEmptyList() throws Exception {
        when(getAllBookingsUseCase.getAllBookings()).thenReturn(List.of());

        mockMvc.perform(get("/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(getAllBookingsUseCase).getAllBookings();
    }

    @Test
    @WithMockUser
    void getBookingById_shouldReturn200() throws Exception {
        BookingResponse response = BookingResponse.builder()
                .id(1L)
                .bookingReference("ABC123")
                .build();

        when(getBookingUseCase.getBooking(1L)).thenReturn(response);

        mockMvc.perform(get("/bookings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.bookingReference").value("ABC123"));

        verify(getBookingUseCase).getBooking(1L);
    }

    @Test
    @WithMockUser
    void createBooking_shouldReturn201() throws Exception {
        CreateBookingRequest request = new CreateBookingRequest();
        request.setSeatId(1L);

        BookingResponse response = BookingResponse.builder()
                .id(1L)
                .bookingReference("ABC123")
                .build();

        when(createBookingUseCase.toResponse(any(CreateBookingRequest.class))).thenReturn(response);

        mockMvc.perform(post("/bookings")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.bookingReference").value("ABC123"));

        verify(createBookingUseCase).toResponse(any(CreateBookingRequest.class));
    }

    @Test
    @WithMockUser
    void cancelBooking_shouldReturn204() throws Exception {
        mockMvc.perform(patch("/bookings/1/cancel"))
                .andExpect(status().isNoContent());

        verify(cancelBookingUseCase).cancelBooking(1L);
    }

    @Test
    @WithMockUser
    void getNextFlight_withUpcoming_shouldReturn200() throws Exception {
        BookingResponse response = BookingResponse.builder()
                .id(1L)
                .bookingReference("ABC123")
                .build();

        when(getNextFlightUseCase.getNextFlight()).thenReturn(response);

        mockMvc.perform(get("/bookings/upcoming"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.bookingReference").value("ABC123"));

        verify(getNextFlightUseCase).getNextFlight();
    }

    @Test
    @WithMockUser
    void getNextFlight_noUpcoming_shouldReturn204() throws Exception {
        when(getNextFlightUseCase.getNextFlight()).thenReturn(null);

        mockMvc.perform(get("/bookings/upcoming"))
                .andExpect(status().isNoContent());

        verify(getNextFlightUseCase).getNextFlight();
    }

    @Test
    @WithMockUser
    void getStatistics_shouldReturn200() throws Exception {
        BookingStatsResponse stats = BookingStatsResponse.builder()
                .totalFlights(5)
                .build();

        when(getStatisticsUseCase.getStatistics()).thenReturn(stats);

        mockMvc.perform(get("/bookings/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalFlights").value(5));

        verify(getStatisticsUseCase).getStatistics();
    }
}