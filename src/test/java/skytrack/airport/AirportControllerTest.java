package skytrack.airport;

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
import skytrack.business.useCase.airport.*;
import skytrack.dto.airport.AirportResponse;
import skytrack.dto.airport.CreateAirportRequest;
import skytrack.dto.airport.GetAllAirports;
import skytrack.dto.airport.UpdateAirportRequest;
import skytrack.presentation.controller.AirportController;
import skytrack.presentation.security.JwtAuthenticationFilter;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AirportController.class)
class AirportControllerTest {

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
    private GetAllAirportsUseCase getAllAirportsUseCase;

    @MockitoBean
    private GetAirportUseCase getAirportUseCase;

    @MockitoBean
    private CreateAirportUseCase createAirportUseCase;

    @MockitoBean
    private UpdateAirportUseCase updateAirportUseCase;

    @MockitoBean
    private ArchiveAirportUseCase archiveAirportUseCase;

    @MockitoBean
    private SearchAirportUseCase searchAirportUseCase;

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
    void getAllAirports_shouldReturn200() throws Exception {
        AirportResponse airport = AirportResponse.builder()
                .id(1L)
                .iataCode("AMS")
                .name("Amsterdam Airport")
                .city("Amsterdam")
                .country("Netherlands")
                .timezone("Europe/Amsterdam")
                .build();

        GetAllAirports response = GetAllAirports.builder()
                .airports(List.of(airport))
                .build();

        when(getAllAirportsUseCase.getAllAirports()).thenReturn(response);

        mockMvc.perform(get("/airports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.airports[0].id").value(1))
                .andExpect(jsonPath("$.airports[0].iataCode").value("AMS"))
                .andExpect(jsonPath("$.airports[0].name").value("Amsterdam Airport"));

        verify(getAllAirportsUseCase).getAllAirports();
    }

    @Test
    @WithMockUser
    void getAirportById_shouldReturn200() throws Exception {
        AirportResponse response = AirportResponse.builder()
                .id(1L)
                .iataCode("AMS")
                .name("Amsterdam Airport")
                .city("Amsterdam")
                .country("Netherlands")
                .timezone("Europe/Amsterdam")
                .build();

        when(getAirportUseCase.getAirportById(1L)).thenReturn(response);

        mockMvc.perform(get("/airports/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.iataCode").value("AMS"))
                .andExpect(jsonPath("$.city").value("Amsterdam"));

        verify(getAirportUseCase).getAirportById(1L);
    }

    @Test
    @WithMockUser
    void createAirport_shouldReturn201() throws Exception {
        CreateAirportRequest request = new CreateAirportRequest(
                "AMS",
                "Amsterdam Airport",
                "Amsterdam",
                "Netherlands",
                "Europe/Amsterdam"
        );

        AirportResponse response = AirportResponse.builder()
                .id(1L)
                .iataCode("AMS")
                .name("Amsterdam Airport")
                .city("Amsterdam")
                .country("Netherlands")
                .timezone("Europe/Amsterdam")
                .build();

        when(createAirportUseCase.createAirport(any(CreateAirportRequest.class))).thenReturn(response);

        mockMvc.perform(post("/airports")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.iataCode").value("AMS"))
                .andExpect(jsonPath("$.name").value("Amsterdam Airport"));

        verify(createAirportUseCase).createAirport(any(CreateAirportRequest.class));
    }

    @Test
    @WithMockUser
    void updateAirport_shouldReturn204() throws Exception {
        UpdateAirportRequest request = new UpdateAirportRequest(
                null,
                "AMS",
                "Amsterdam Airport",
                "Amsterdam",
                "Netherlands",
                "Europe/Amsterdam"
        );

        mockMvc.perform(put("/airports/1")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(updateAirportUseCase).updateAirport(argThat(updatedRequest ->
                updatedRequest.getId().equals(1L)
        ));
    }

    @Test
    @WithMockUser
    void archiveAirport_shouldReturn204() throws Exception {
        mockMvc.perform(patch("/airports/1"))
                .andExpect(status().isNoContent());

        verify(archiveAirportUseCase).archiveAirport(1L);
    }

    @Test
    @WithMockUser
    void searchAirports_shouldReturn200() throws Exception {
        AirportResponse response = AirportResponse.builder()
                .id(1L)
                .iataCode("AMS")
                .name("Amsterdam Airport")
                .city("Amsterdam")
                .country("Netherlands")
                .timezone("Europe/Amsterdam")
                .build();

        when(searchAirportUseCase.searchAirport("ams")).thenReturn(List.of(response));

        mockMvc.perform(get("/airports/search")
                        .param("input", "ams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].iataCode").value("AMS"));

        verify(searchAirportUseCase).searchAirport("ams");
    }
}