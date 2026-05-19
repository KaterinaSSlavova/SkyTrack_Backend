package skytrack.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import skytrack.business.exception.flight.InvalidFlightException;
import skytrack.business.service.FlightValidationService;
import skytrack.persistence.entity.AirportEntity;
import skytrack.persistence.entity.FlightEntity;
import skytrack.persistence.entity.FlightStatusEntity;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class FlightValidationServiceTest {
    private final FlightValidationService validator = new FlightValidationService();

    private FlightEntity buildValidFlight() {
        AirportEntity departure = AirportEntity.builder()
                .iataCode("AMS")
                .name("Amsterdam Airport")
                .city("Amsterdam")
                .country("Netherlands")
                .timezone("Europe/Amsterdam")
                .build();

        AirportEntity arrival = AirportEntity.builder()
                .iataCode("LHR")
                .name("Heathrow Airport")
                .city("London")
                .country("United Kingdom")
                .timezone("Europe/London")
                .build();

        FlightStatusEntity status = new FlightStatusEntity();
        status.setName("SCHEDULED");

        FlightEntity flight = new FlightEntity();
        flight.setFlightNumber("SK123");
        flight.setDepartureAirport(departure);
        flight.setArrivalAirport(arrival);
        flight.setCapacity(180);
        flight.setDepartureTimeUTC(Instant.parse("2026-06-01T10:00:00Z"));
        flight.setArrivalTimeUTC(Instant.parse("2026-06-01T12:00:00Z"));
        flight.setPrice(new BigDecimal("100.00"));
        flight.setStatus(status);
        return flight;
    }

    @Test
    void validateFlight_shouldPass_whenFlightIsValid() {
        assertDoesNotThrow(() -> validator.validateFlight(buildValidFlight()));
    }

    @Test
    void validateFlight_shouldThrow_whenFlightNumberIsNull() {
        FlightEntity flight = buildValidFlight();
        flight.setFlightNumber(null);
        assertThrows(InvalidFlightException.class, () -> validator.validateFlight(flight));
    }

    @Test
    void validateFlight_shouldThrow_whenFlightNumberIsBlank() {
        FlightEntity flight = buildValidFlight();
        flight.setFlightNumber("   ");
        assertThrows(InvalidFlightException.class, () -> validator.validateFlight(flight));
    }

    @Test
    void validateFlight_shouldThrow_whenDepartureAirportIsNull() {
        FlightEntity flight = buildValidFlight();
        flight.setDepartureAirport(null);
        assertThrows(InvalidFlightException.class, () -> validator.validateFlight(flight));
    }

    @Test
    void validateFlight_shouldThrow_whenArrivalAirportIsNull() {
        FlightEntity flight = buildValidFlight();
        flight.setArrivalAirport(null);
        assertThrows(InvalidFlightException.class, () -> validator.validateFlight(flight));
    }

    @Test
    void validateFlight_shouldThrow_whenDepartureAndArrivalAreTheSame() {
        FlightEntity flight = buildValidFlight();
        flight.setArrivalAirport(flight.getDepartureAirport());
        assertThrows(InvalidFlightException.class, () -> validator.validateFlight(flight));
    }

    @Test
    void validateFlight_shouldThrow_whenCapacityIsNull() {
        FlightEntity flight = buildValidFlight();
        flight.setCapacity(null);
        assertThrows(InvalidFlightException.class, () -> validator.validateFlight(flight));
    }

    @Test
    void validateFlight_shouldThrow_whenCapacityIsZero() {
        FlightEntity flight = buildValidFlight();
        flight.setCapacity(0);
        assertThrows(InvalidFlightException.class, () -> validator.validateFlight(flight));
    }

    @Test
    void validateFlight_shouldThrow_whenCapacityIsNegative() {
        FlightEntity flight = buildValidFlight();
        flight.setCapacity(-1);
        assertThrows(InvalidFlightException.class, () -> validator.validateFlight(flight));
    }

    @Test
    void validateFlight_shouldThrow_whenDepartureTimeIsNull() {
        FlightEntity flight = buildValidFlight();
        flight.setDepartureTimeUTC(null);
        assertThrows(InvalidFlightException.class, () -> validator.validateFlight(flight));
    }

    @Test
    void validateFlight_shouldThrow_whenArrivalTimeIsNull() {
        FlightEntity flight = buildValidFlight();
        flight.setArrivalTimeUTC(null);
        assertThrows(InvalidFlightException.class, () -> validator.validateFlight(flight));
    }

    @Test
    void validateFlight_shouldThrow_whenArrivalIsBeforeDeparture() {
        FlightEntity flight = buildValidFlight();
        flight.setArrivalTimeUTC(Instant.parse("2026-06-01T08:00:00Z"));
        assertThrows(InvalidFlightException.class, () -> validator.validateFlight(flight));
    }

    @Test
    void validateFlight_shouldThrow_whenPriceIsNull() {
        FlightEntity flight = buildValidFlight();
        flight.setPrice(null);
        assertThrows(InvalidFlightException.class, () -> validator.validateFlight(flight));
    }

    @Test
    void validateFlight_shouldThrow_whenPriceIsZero() {
        FlightEntity flight = buildValidFlight();
        flight.setPrice(BigDecimal.ZERO);
        assertThrows(InvalidFlightException.class, () -> validator.validateFlight(flight));
    }

    @Test
    void validateFlight_shouldThrow_whenPriceIsNegative() {
        FlightEntity flight = buildValidFlight();
        flight.setPrice(new BigDecimal("-10.00"));
        assertThrows(InvalidFlightException.class, () -> validator.validateFlight(flight));
    }

    @Test
    void validateFlight_shouldThrow_whenStatusIsNull() {
        FlightEntity flight = buildValidFlight();
        flight.setStatus(null);
        assertThrows(InvalidFlightException.class, () -> validator.validateFlight(flight));
    }

    @Test
    void validateFlight_shouldThrow_whenStatusNameIsNull() {
        FlightEntity flight = buildValidFlight();
        flight.getStatus().setName(null);
        assertThrows(InvalidFlightException.class, () -> validator.validateFlight(flight));
    }

    @Test
    void validateFlight_shouldThrow_whenStatusNameIsBlank() {
        FlightEntity flight = buildValidFlight();
        flight.getStatus().setName("   ");
        assertThrows(InvalidFlightException.class, () -> validator.validateFlight(flight));
    }
}
