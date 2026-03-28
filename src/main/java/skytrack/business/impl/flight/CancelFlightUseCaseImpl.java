package skytrack.business.impl.flight;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.exception.FlightNotFoundException;
import skytrack.business.exception.FlightStatusNotFoundException;
import skytrack.business.repository.FlightRepository;
import skytrack.business.repository.FlightStatusRepository;
import skytrack.business.useCase.flight.CancelFlightUseCase;
import skytrack.domain.entity.Flight;
import skytrack.domain.entity.FlightStatus;

@Service
@RequiredArgsConstructor
public class CancelFlightUseCaseImpl implements CancelFlightUseCase {
    private final FlightRepository flightRepository;
    private final FlightStatusRepository flightStatusRepository;

    @Override
    public void cancelFlight(Long flightId) {
        Flight flight = flightRepository.findFlightById(flightId)
                .orElseThrow(() -> new FlightNotFoundException(flightId));
        FlightStatus cancelStatus = flightStatusRepository.findFlightStatusByName("CANCELLED")
                .orElseThrow(() -> new FlightStatusNotFoundException(flightId));
        flight.setStatus(cancelStatus);
        flightRepository.updateFlight(flight);
    }
}
