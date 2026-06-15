package skytrack.flight;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import skytrack.persistence.entity.*;
import skytrack.persistence.enumeration.FlightStatus;
import skytrack.persistence.enumeration.Role;
import skytrack.persistence.repo.*;

import java.math.BigDecimal;
import java.time.Instant;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class FlightControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DuffelRepository duffelRepository;

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void getAllDuffelFlights_withAdmin_shouldReturnOk() throws Exception {
        createAdmin();

        mockMvc.perform(get("/flights/duffel")
                        .with(user("admin@test.com").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    void getAllDuffelFlights_withPassenger_shouldReturnForbidden() throws Exception {
        createPassenger();

        mockMvc.perform(get("/flights/duffel")
                        .with(user("passenger@test.com").roles("PASSENGER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void getDuffelFlightById_whenFlightExists_shouldReturnFlight() throws Exception {
        createAdmin();
        DuffelFlightEntity flight = createDuffelFlight();

        mockMvc.perform(get("/flights/duffel/" + flight.getId())
                        .with(user("admin@test.com").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flightNumber").value("ST123"))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    void getDuffelFlightById_whenFlightDoesNotExist_shouldReturnNotFound() throws Exception {
        createAdmin();

        mockMvc.perform(get("/flights/duffel/666666666")
                        .with(user("admin@test.com").roles("ADMIN")))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateFlightGate_withAdmin_shouldReturnNoContent() throws Exception {
        createAdmin();
        DuffelFlightEntity flight = createDuffelFlight();

        mockMvc.perform(patch("/flights/duffel/" + flight.getId() + "/gate")
                        .param("gate", "B2")
                        .with(user("admin@test.com").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateFlightGate_withPassenger_shouldReturnForbidden() throws Exception {
        createPassenger();
        DuffelFlightEntity flight = createDuffelFlight();

        mockMvc.perform(patch("/flights/duffel/" + flight.getId() + "/gate")
                        .param("gate", "B2")
                        .with(user("passenger@test.com").roles("PASSENGER"))
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateFlightStatus_withAdmin_shouldReturnNoContent() throws Exception {
        createAdmin();
        DuffelFlightEntity flight = createDuffelFlight();

        mockMvc.perform(patch("/flights/duffel/" + flight.getId() + "/status")
                        .param("status", "DELAYED")
                        .param("newDepTime", "2026-12-01T10:00:00")
                        .with(user("admin@test.com").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateFlightStatus_withPassenger_shouldReturnForbidden() throws Exception {
        createPassenger();
        DuffelFlightEntity flight = createDuffelFlight();

        mockMvc.perform(patch("/flights/duffel/" + flight.getId() + "/status")
                        .param("status", "DELAYED")
                        .with(user("passenger@test.com").roles("PASSENGER"))
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    void duffelSearchFlights_withAdmin_shouldReturnForbidden() throws Exception {
        createAdmin();

        mockMvc.perform(get("/flights/duffel/search")
                        .param("departureIata", "TST")
                        .param("arrivalIata", "UPD")
                        .param("departureDate", "2026-12-01")
                        .with(user("admin@test.com").roles("ADMIN")))
                .andExpect(status().isForbidden());
    }

    @Test
    void createFlight_withAdmin_shouldReturnForbidden() throws Exception {
        createAdmin();

        String body = "{}";

        mockMvc.perform(post("/flights/duffel/save")
                        .with(user("admin@test.com").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden());
    }

    private UserEntity createPassenger() {
        RoleEntity role = roleRepository.save(
                RoleEntity.builder()
                        .roleName(Role.PASSENGER)
                        .build()
        );

        return userRepository.save(
                UserEntity.builder()
                        .firstName("Test")
                        .lastName("Passenger")
                        .email("passenger@test.com")
                        .passwordHash("password")
                        .role(role)
                        .build()
        );
    }

    private UserEntity createAdmin() {
        RoleEntity role = roleRepository.save(
                RoleEntity.builder()
                        .roleName(Role.ADMIN)
                        .build()
        );

        return userRepository.save(
                UserEntity.builder()
                        .firstName("Test")
                        .lastName("Admin")
                        .email("admin@test.com")
                        .passwordHash("password")
                        .role(role)
                        .build()
        );
    }

    private DuffelFlightEntity createDuffelFlight() {
        AirportEntity departureAirport = airportRepository.save(
                AirportEntity.builder()
                        .iataCode("TST")
                        .name("Test Departure Airport")
                        .city("Test City")
                        .country("Test Country")
                        .timezone("Europe/Amsterdam")
                        .build()
        );

        AirportEntity arrivalAirport = airportRepository.save(
                AirportEntity.builder()
                        .iataCode("UPD")
                        .name("Test Arrival Airport")
                        .city("Updated City")
                        .country("Updated Country")
                        .timezone("Europe/Amsterdam")
                        .build()
        );

        return duffelRepository.save(
                DuffelFlightEntity.builder()
                        .externalId("test-flight-id")
                        .flightNumber("ST123")
                        .departureIataCode("TST")
                        .arrivalIataCode("UPD")
                        .departureAirport(departureAirport)
                        .arrivalAirport(arrivalAirport)
                        .departureTime(Instant.now().plusSeconds(3600))
                        .arrivalTime(Instant.now().plusSeconds(7200))
                        .departureTimezone("Europe/Amsterdam")
                        .arrivalTimezone("Europe/Amsterdam")
                        .gate("A1")
                        .price(new BigDecimal("100.00"))
                        .currency("EUR")
                        .status(FlightStatus.SCHEDULED)
                        .build()
        );
    }
}
