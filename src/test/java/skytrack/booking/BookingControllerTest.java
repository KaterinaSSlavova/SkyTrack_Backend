package skytrack.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import skytrack.persistence.entity.*;
import skytrack.persistence.enumeration.Role;
import skytrack.persistence.repo.*;

import java.math.BigDecimal;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BookingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private DuffelRepository duffelFlightRepository;

    @Test
    void getAllBookings_withPassenger_shouldReturnOk() throws Exception {
        createPassenger();

        mockMvc.perform(get("/bookings")
                        .with(user("passenger@test.com").roles("PASSENGER")))
                .andExpect(status().isOk());
    }

    @Test
    void getAllBookings_withAdmin_shouldReturnOk() throws Exception {
        createAdmin();

        mockMvc.perform(get("/bookings")
                        .with(user("admin@test.com").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    void getBooking_whenIdDoesNotExist_shouldReturnNotFound() throws Exception {
        createPassenger();

        mockMvc.perform(get("/bookings/666666666")
                        .with(user("passenger@test.com").roles("PASSENGER")))
                .andExpect(status().isNotFound());
    }

    @Test
    void createBooking_withAdmin_shouldReturnForbidden() throws Exception {
        createAdmin();

        String body = """
                {
                  "passenger": null,
                  "flight": null,
                  "seatId": 1
                }
                """;

        mockMvc.perform(post("/bookings")
                        .with(user("admin@test.com").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden());
    }

    @Test
    void cancelBooking_whenBookingExists_shouldReturnNoContent() throws Exception {
        BookingEntity booking = createBookingForPassenger();

        mockMvc.perform(patch("/bookings/" + booking.getId() + "/cancel")
                        .with(user("passenger@test.com").roles("PASSENGER"))
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    void cancelBooking_withAdmin_shouldReturnForbidden() throws Exception {
        createAdmin();

        mockMvc.perform(patch("/bookings/1/cancel")
                        .with(user("admin@test.com").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    void cancelBooking_whenBookingDoesNotExist_shouldReturnNotFound() throws Exception {
        createPassenger();

        mockMvc.perform(patch("/bookings/666666666/cancel")
                        .with(user("passenger@test.com").roles("PASSENGER"))
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getQRCode_withPassenger_shouldReturnPng() throws Exception {
        createPassenger();

        mockMvc.perform(get("/bookings/TEST-REF/qr")
                        .with(user("passenger@test.com").roles("PASSENGER")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG));
    }

    @Test
    void getQRCode_withoutUser_shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/bookings/TEST-REF/qr"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getUpcoming_withPassenger_shouldReturnOkOrNoContent() throws Exception {
        createPassenger();

        mockMvc.perform(get("/bookings/upcoming")
                        .with(user("passenger@test.com").roles("PASSENGER")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getUpcoming_withAdmin_shouldReturnForbidden() throws Exception {
        createAdmin();

        mockMvc.perform(get("/bookings/upcoming")
                        .with(user("admin@test.com").roles("ADMIN")))
                .andExpect(status().isForbidden());
    }

    @Test
    void getStatistics_withPassenger_shouldReturnOk() throws Exception {
        createPassenger();

        mockMvc.perform(get("/bookings/statistics")
                        .with(user("passenger@test.com").roles("PASSENGER")))
                .andExpect(status().isOk());
    }

    @Test
    void getStatistics_withAdmin_shouldReturnForbidden() throws Exception {
        createAdmin();

        mockMvc.perform(get("/bookings/statistics")
                        .with(user("admin@test.com").roles("ADMIN")))
                .andExpect(status().isForbidden());
    }

    private UserEntity createPassenger() {
        RoleEntity role = roleRepository.save(RoleEntity.builder().roleName(Role.PASSENGER).build());

        return userRepository.save(
                UserEntity.builder()
                        .firstName("Test")
                        .lastName("Passenger")
                        .email("passenger@test.com")
                        .passwordHash("password")
                        .role(role)
                        .build());
    }

    private UserEntity createAdmin() {
        RoleEntity role = roleRepository.save(RoleEntity.builder().roleName(Role.ADMIN).build());

        return userRepository.save(
                UserEntity.builder()
                        .firstName("Test")
                        .lastName("Admin")
                        .email("admin@test.com")
                        .passwordHash("password")
                        .role(role)
                        .build());
    }

    private BookingEntity createBookingForPassenger() {
        UserEntity passengerUser = createPassenger();

        SeatEntity seat = seatRepository.save(
                SeatEntity.builder()
                        .seatNumber("12A")
                        .window(true)
                        .aisle(false)
                        .extraLegroom(false)
                        .build());

        DuffelFlightEntity flight = duffelFlightRepository.save(
                DuffelFlightEntity.builder()
                        .externalId("test-flight-id")
                        .flightNumber("ST123")
                        .departureIataCode("TST")
                        .arrivalIataCode("UPD")
                        .departureTimezone("Europe/Amsterdam")
                        .arrivalTimezone("Europe/Amsterdam")
                        .price(new BigDecimal("100.00"))
                        .currency("EUR")
                        .build());

        return bookingRepository.save(
                BookingEntity.builder()
                        .user(passengerUser)
                        .externalFlight(flight)
                        .seat(seat)
                        .basePrice(new BigDecimal("100.00"))
                        .totalPrice(new BigDecimal("100.00"))
                        .currency("EUR")
                        .bookingReference("TEST-REF")
                        .archived(false)
                        .build());
    }
}
