package skytrack;

import com.stripe.exception.ApiException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import skytrack.business.exception.airport.AirportAlreadyExistsException;
import skytrack.business.exception.airport.InvalidAirportException;
import skytrack.business.exception.flight.FlightNotFoundException;
import skytrack.business.exception.flight.FlightStatusNotFoundException;
import skytrack.business.exception.flight.InvalidFlightException;
import skytrack.business.exception.user.*;
import skytrack.dto.exception.ErrorResponse;
import skytrack.presentation.controller.GlobalExceptionHandler;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {
    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleRefreshTokenException_returns401WithCorrectCode() {
        var ex = new RefreshTokenException();
        ResponseEntity<ErrorResponse> response = handler.handleRefreshTokenException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("REFRESH_TOKEN_EXPIRATION");
        assertThat(response.getBody().getMessage()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(401);
        assertThat(response.getBody().getTimestamp()).isNotNull();
    }

    @Test
    void handleTooManyRequestsException_returns429WithCorrectCode() {
        var ex = new TooManyRequestsException();
        ResponseEntity<ErrorResponse> response = handler.handleUserNotFoundException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.TOO_MANY_REQUESTS);
        assertThat(response.getBody().getError()).isEqualTo("TOO_MANY_REQUESTS");
        assertThat(response.getBody().getStatus()).isEqualTo(429);
    }

    @Test
    void handleUserNotFoundException_returns404WithCorrectCode() {
        var ex = new UserNotFoundException(1L);
        ResponseEntity<ErrorResponse> response = handler.handleUserNotFoundException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getError()).isEqualTo("USER_NOT_FOUND");
        assertThat(response.getBody().getMessage()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(404);
    }

    @Test
    void handleRoleNotFoundException_returns404WithCorrectCode() {
        var ex = new RoleNotFoundException("ROLE");
        ResponseEntity<ErrorResponse> response = handler.handleRoleNotFoundException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getError()).isEqualTo("ROLE_NOT_FOUND");
    }

    @Test
    void handleUserEmailAlreadyExistsException_returns409WithCorrectCode() {
        var ex = new UserEmailAlreadyExistsException("user@mail.bg");
        ResponseEntity<ErrorResponse> response = handler.handleUserEmailAlreadyExistsException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody().getError()).isEqualTo("EMAIL_ALREADY_EXISTS");
    }

    @Test
    void handleUserEmailInvalidException_returns401WithCorrectCode() {
        var ex = new UserEmailInvalidException("user@emaill.con");
        ResponseEntity<ErrorResponse> response = handler.handleUserEmailInvalidException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody().getError()).isEqualTo("INVALID_EMAIL");
    }

    @Test
    void handleUserPasswordInvalidException_returns401WithCorrectCode() {
        var ex = new UserPasswordInvalidException();
        ResponseEntity<ErrorResponse> response = handler.handleUserPasswordInvalidException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody().getError()).isEqualTo("INVALID_PASSWORD");
    }

    @Test
    void handleUserTooYoungException_returns409WithInvalidAgeCode() {
        var ex = new UserTooYoungException();
        ResponseEntity<ErrorResponse> response = handler.handleUserTooYoungException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody().getError()).isEqualTo("INVALID_USER_AGE");
    }

    @Test
    void handleUserTooOldException_returns409WithInvalidAgeCode() {
        var ex = new UserTooOldException();
        ResponseEntity<ErrorResponse> response = handler.handleUserTooOldException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody().getError()).isEqualTo("INVALID_USER_AGE");
    }

    @Test
    void handleInvalidPassportException_returns400WithCorrectCode() {
        var ex = new InvalidPassportException("Invalid passport");
        ResponseEntity<ErrorResponse> response = handler.handleInvalidPassportException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getError()).isEqualTo("INVALID_PASSPORT");
    }

    @Test
    void handleFlightNotFoundException_returns404WithCorrectCode() {
        var ex = new FlightNotFoundException("123456");
        ResponseEntity<ErrorResponse> response = handler.handleFlightNotFoundException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getError()).isEqualTo("FLIGHT_NOT_FOUND");
    }

    @Test
    void handleFlightStatusNotFoundException_returns404WithCorrectCode() {
        var ex = new FlightStatusNotFoundException(1L);
        ResponseEntity<ErrorResponse> response = handler.handleFlightStatusNotFoundException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getError()).isEqualTo("FLIGHT_STATUS_NOT_FOUND");
    }

    @Test
    void handleInvalidFlightException_returns400WithCorrectCode() {
        var ex = new InvalidFlightException("Invalid flight data");
        ResponseEntity<ErrorResponse> response = handler.handleInvalidFlightException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getError()).isEqualTo("INVALID_FLIGHT");
    }

    @Test
    void handleInvalidAirportException_returns400WithCorrectCode() {
        var ex = new InvalidAirportException("Invalid airport");
        ResponseEntity<ErrorResponse> response = handler.handleInvalidAirportException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getError()).isEqualTo("INVALID_AIRPORT");
    }

    @Test
    void handleAirportAlreadyExistsException_returns409WithCorrectCode() {
        var ex = new AirportAlreadyExistsException("Airport exists");
        ResponseEntity<ErrorResponse> response = handler.handleAirportAlreadyExistsException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody().getError()).isEqualTo("AIRPORT_ALREADY_EXISTS");
    }

    @Test
    void handleStripeException_returns502WithCorrectCode() throws Exception {
        var ex = new ApiException("Card declined", "req_123", "card_declined", 402, null);
        ResponseEntity<ErrorResponse> response = handler.handleStripeException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_GATEWAY);
        assertThat(response.getBody().getError()).isEqualTo("PAYMENT_ERROR");
        assertThat(response.getBody().getStatus()).isEqualTo(502);
    }

    @Test
    void handleMethodArgumentNotValidException_returns400WithFieldErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("userDto", "email", "must not be blank");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<ErrorResponse> response = handler.handleMethodArgumentNotValidException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getError()).isEqualTo("VALIDATION_FAILED");
        assertThat(response.getBody().getFieldErrors()).containsEntry("email", "must not be blank");
    }

    @Test
    void handleMethodArgumentNotValidException_multipleFieldErrors_allCaptured() {
        BindingResult bindingResult = mock(BindingResult.class);
        List<FieldError> errors = List.of(
                new FieldError("userDto", "email", "must not be blank"),
                new FieldError("userDto", "password", "too short")
        );
        when(bindingResult.getFieldErrors()).thenReturn(errors);

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<ErrorResponse> response = handler.handleMethodArgumentNotValidException(ex);

        assertThat(response.getBody().getFieldErrors()).hasSize(2);
        assertThat(response.getBody().getFieldErrors()).containsEntry("password", "too short");
    }

    @Test
    void handleConstraintViolationException_returns400WithFieldErrors() {
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        jakarta.validation.Path path = mock(jakarta.validation.Path.class);
        when(path.toString()).thenReturn("createUser.age");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("must be positive");

        ConstraintViolationException ex = new ConstraintViolationException(Set.of(violation));
        ResponseEntity<ErrorResponse> response = handler.handleConstraintViolationException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getError()).isEqualTo("VALIDATION_FAILED");
        assertThat(response.getBody().getFieldErrors()).containsEntry("createUser.age", "must be positive");
    }

    @Test
    void handleUnexpectedException_returns500WithGenericMessage() {
        var ex = new RuntimeException("Something exploded");
        ResponseEntity<ErrorResponse> response = handler.handleUnexpectedException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().getError()).isEqualTo("INTERNAL_SERVER_ERROR");
        assertThat(response.getBody().getMessage()).isEqualTo("Something went wrong.");
        assertThat(response.getBody().getStatus()).isEqualTo(500);
    }

    @Test
    void allHandlers_includeNonNullTimestamp() {
        var ex = new UserNotFoundException("x");
        ResponseEntity<ErrorResponse> response = handler.handleUserNotFoundException(ex);
        assertThat(response.getBody().getTimestamp()).isNotNull();
    }
}
