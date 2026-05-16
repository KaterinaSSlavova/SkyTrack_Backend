package skytrack.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import skytrack.business.exception.airport.InvalidAirportException;
import skytrack.business.service.AirportValidationService;
import skytrack.persistence.entity.AirportEntity;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class AirportValidationServiceTest {
    private final AirportValidationService validator = new AirportValidationService();

    private AirportEntity buildValidAirport() {
        return AirportEntity.builder()
                .iataCode("AMS")
                .name("Amsterdam Airport")
                .country("Netherlands")
                .city("Amsterdam")
                .timezone("Europe/Amsterdam")
                .build();
    }

    @Test
    void validateAirport_shouldPass_whenAirportIsValid() {
        assertDoesNotThrow(() -> validator.validateAirport(buildValidAirport()));
    }

    @Test
    void validateAirport_shouldThrow_whenIataCodeIsNull() {
        AirportEntity airport = buildValidAirport();
        airport.setIataCode(null);
        assertThrows(InvalidAirportException.class, () -> validator.validateAirport(airport));
    }

    @Test
    void validateAirport_shouldThrow_whenIataCodeIsLowercase() {
        AirportEntity airport = buildValidAirport();
        airport.setIataCode("ams");
        assertThrows(InvalidAirportException.class, () -> validator.validateAirport(airport));
    }

    @Test
    void validateAirport_shouldThrow_whenIataCodeIsTooShort() {
        AirportEntity airport = buildValidAirport();
        airport.setIataCode("AM");
        assertThrows(InvalidAirportException.class, () -> validator.validateAirport(airport));
    }

    @Test
    void validateAirport_shouldThrow_whenIataCodeIsTooLong() {
        AirportEntity airport = buildValidAirport();
        airport.setIataCode("AMST");
        assertThrows(InvalidAirportException.class, () -> validator.validateAirport(airport));
    }

    @Test
    void validateAirport_shouldThrow_whenNameIsNull() {
        AirportEntity airport = buildValidAirport();
        airport.setName(null);
        assertThrows(InvalidAirportException.class, () -> validator.validateAirport(airport));
    }

    @Test
    void validateAirport_shouldThrow_whenNameIsBlank() {
        AirportEntity airport = buildValidAirport();
        airport.setName("   ");
        assertThrows(InvalidAirportException.class, () -> validator.validateAirport(airport));
    }

    @Test
    void validateAirport_shouldThrow_whenCountryIsNull() {
        AirportEntity airport = buildValidAirport();
        airport.setCountry(null);
        assertThrows(InvalidAirportException.class, () -> validator.validateAirport(airport));
    }

    @Test
    void validateAirport_shouldThrow_whenCountryIsBlank() {
        AirportEntity airport = buildValidAirport();
        airport.setCountry("   ");
        assertThrows(InvalidAirportException.class, () -> validator.validateAirport(airport));
    }

    @Test
    void validateAirport_shouldThrow_whenCityIsNull() {
        AirportEntity airport = buildValidAirport();
        airport.setCity(null);
        assertThrows(InvalidAirportException.class, () -> validator.validateAirport(airport));
    }

    @Test
    void validateAirport_shouldThrow_whenCityIsBlank() {
        AirportEntity airport = buildValidAirport();
        airport.setCity("   ");
        assertThrows(InvalidAirportException.class, () -> validator.validateAirport(airport));
    }

    @Test
    void validateAirport_shouldThrow_whenTimezoneIsNull() {
        AirportEntity airport = buildValidAirport();
        airport.setTimezone(null);
        assertThrows(InvalidAirportException.class, () -> validator.validateAirport(airport));
    }

    @Test
    void validateAirport_shouldThrow_whenTimezoneIsBlank() {
        AirportEntity airport = buildValidAirport();
        airport.setTimezone("   ");
        assertThrows(InvalidAirportException.class, () -> validator.validateAirport(airport));
    }
}
