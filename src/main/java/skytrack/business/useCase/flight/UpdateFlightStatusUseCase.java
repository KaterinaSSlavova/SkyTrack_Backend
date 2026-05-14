package skytrack.business.useCase.flight;

import skytrack.persistence.enumeration.FlightStatus;

import java.time.LocalDateTime;

public interface UpdateFlightStatusUseCase {
    void updateFlightStatus(Long id, FlightStatus status, LocalDateTime departureTime);
}
