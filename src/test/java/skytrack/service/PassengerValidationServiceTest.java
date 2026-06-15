package skytrack.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import skytrack.business.exception.user.InvalidPassportException;
import skytrack.business.exception.user.UserTooOldException;
import skytrack.business.exception.user.UserTooYoungException;
import skytrack.business.service.PassengerValidationService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class PassengerValidationServiceTest {
    private final PassengerValidationService validationService = new PassengerValidationService();

    @Test
    void validateAge_shouldPass_whenAgeIsValid() {
        assertDoesNotThrow(() ->
                validationService.validateAge(LocalDate.now().minusYears(25)));
    }

    @Test
    void validateAge_shouldThrow_whenUserIsTooYoung() {
        assertThrows(UserTooYoungException.class,
                () -> validationService.validateAge(LocalDate.now().minusYears(15)));
    }

    @Test
    void validateAge_shouldPass_whenUserIsExactly16() {
        assertDoesNotThrow(() ->
                validationService.validateAge(LocalDate.now().minusYears(16)));
    }

    @Test
    void validateAge_shouldThrow_whenUserIsTooOld() {
        assertThrows(UserTooOldException.class,
                () -> validationService.validateAge(LocalDate.now().minusYears(121)));
    }

    @Test
    void validateAge_shouldPass_whenUserIsExactly120() {
        assertDoesNotThrow(() ->
                validationService.validateAge(LocalDate.now().minusYears(120)));
    }

    @Test
    void validatePassportNumber_shouldPass_whenPassportIsValid() {
        assertDoesNotThrow(() ->
                validationService.validatePassportNumber("AB123456"));
    }

    @Test
    void validatePassportNumber_shouldThrow_whenPassportIsNull() {
        assertThrows(InvalidPassportException.class,
                () -> validationService.validatePassportNumber(null));
    }

    @Test
    void validatePassportNumber_shouldThrow_whenPassportIsBlank() {
        assertThrows(InvalidPassportException.class,
                () -> validationService.validatePassportNumber(" "));
    }

    @Test
    void validatePassportNumber_shouldThrow_whenPassportIsTooShort() {
        assertThrows(InvalidPassportException.class,
                () -> validationService.validatePassportNumber("ABC12"));
    }

    @Test
    void validatePassportNumber_shouldThrow_whenPassportIsTooLong() {
        assertThrows(InvalidPassportException.class,
                () -> validationService.validatePassportNumber("ABCDEFGHIJKLM"));
    }

    @Test
    void validatePassportNumber_shouldThrow_whenPassportContainsSpecialCharacters() {
        assertThrows(InvalidPassportException.class,
                () -> validationService.validatePassportNumber("AB@123"));
    }

    @Test
    void validatePassportExpiration_shouldPass_whenPassportIsValidForMoreThanSixMonths() {
        LocalDate departure = LocalDate.of(2026, 6, 1);
        LocalDate expiration = departure.plusMonths(7);

        assertDoesNotThrow(() ->
                validationService.validatePassportExpiration(expiration, departure));
    }

    @Test
    void validatePassportExpiration_shouldThrow_whenExpirationDateIsNull() {
        LocalDate departure = LocalDate.of(2026, 6, 1);

        assertThrows(InvalidPassportException.class,
                () -> validationService.validatePassportExpiration(null, departure));
    }

    @Test
    void validatePassportExpiration_shouldThrow_whenPassportExpiresBeforeDeparture() {
        LocalDate departure = LocalDate.of(2026, 6, 1);
        LocalDate expiration = LocalDate.of(2026, 5, 1);

        assertThrows(InvalidPassportException.class,
                () -> validationService.validatePassportExpiration(expiration, departure));
    }

    @Test
    void validatePassportExpiration_shouldThrow_whenPassportExpiresOnDepartureDate() {
        LocalDate departure = LocalDate.of(2026, 6, 1);

        assertThrows(InvalidPassportException.class,
                () -> validationService.validatePassportExpiration(departure, departure));
    }

    @Test
    void validatePassportExpiration_shouldThrow_whenPassportIsValidForLessThanSixMonths() {
        LocalDate departure = LocalDate.of(2026, 6, 1);
        LocalDate expiration = departure.plusMonths(5);

        assertThrows(InvalidPassportException.class,
                () -> validationService.validatePassportExpiration(expiration, departure));
    }

    @Test
    void validatePassportExpiration_shouldThrow_whenPassportIsValidForExactlySixMonths() {
        LocalDate departure = LocalDate.of(2026, 6, 1);
        LocalDate expiration = departure.plusMonths(6);

        assertThrows(InvalidPassportException.class,
                () -> validationService.validatePassportExpiration(expiration, departure));
    }
}
