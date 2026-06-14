package skytrack.seat;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import skytrack.persistence.entity.*;
import skytrack.persistence.enumeration.FlightStatus;
import skytrack.persistence.enumeration.Role;
import skytrack.persistence.repo.*;

import java.math.BigDecimal;
import java.time.Instant;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SeatControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private DuffelRepository duffelRepository;

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void getSeat_whenSeatExists_shouldReturnSeat() throws Exception {
        createPassenger();
        DuffelFlightEntity flight = createFlight();
        SeatEntity seat = seatRepository.findAll().get(0);

        mockMvc.perform(get("/seats/" + seat.getId() + "/flight/" + flight.getId())
                        .with(user("passenger@test.com").roles("PASSENGER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(seat.getId()))
                .andExpect(jsonPath("$.seatNumber").value(seat.getSeatNumber()));
    }

    @Test
    void getSeat_whenSeatDoesNotExist_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/seats/666666666/flight/1")
                        .with(user("passenger@test.com").roles("PASSENGER")))
                .andExpect(status().isNotFound());
    }

    @Test
    void getSeatMap_whenFlightIdExists_shouldReturnSeatMap() throws Exception {
        createPassenger();
        DuffelFlightEntity flight = createFlight();

        mockMvc.perform(get("/seats/" + flight.getId())
                        .with(user("passenger@test.com").roles("PASSENGER")))
                .andExpect(status().isOk());
    }

    @Test
    void getSeatMap_whenFlightIdDoesNotExist_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/seats/666666666")
                        .with(user("passenger@test.com").roles("PASSENGER")))
                .andExpect(status().isNotFound());
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

    private DuffelFlightEntity createFlight() {
        AirportEntity departureAirport = airportRepository.save(
                AirportEntity.builder()
                        .iataCode("TST")
                        .name("Test Departure Airport")
                        .city("Test City")
                        .country("Test Country")
                        .timezone("Europe/Amsterdam")
                        .build());

        AirportEntity arrivalAirport = airportRepository.save(
                AirportEntity.builder()
                        .iataCode("UPD")
                        .name("Test Arrival Airport")
                        .city("Updated City")
                        .country("Updated Country")
                        .timezone("Europe/Amsterdam")
                        .build());

        return duffelRepository.save(
                DuffelFlightEntity.builder()
                        .flightNumber("ST123")
                        .departureAirport(departureAirport)
                        .arrivalAirport(arrivalAirport)
                        .departureTime(Instant.now().plusSeconds(3600))
                        .arrivalTime(Instant.now().plusSeconds(7200))
                        .gate("A1")
                        .price(new BigDecimal("100.00"))
                        .status(FlightStatus.SCHEDULED)
                        .build());
    }
}
