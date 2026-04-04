package skytrack.business.impl.flight;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.exception.FlightNotFoundException;
import skytrack.business.exception.FlightStatusNotFoundException;
import skytrack.business.useCase.flight.CancelFlightUseCase;
import skytrack.persistence.entity.FlightEntity;
import skytrack.persistence.entity.FlightStatusEntity;
import skytrack.persistence.repo.FlightRepository;
import skytrack.persistence.repo.FlightStatusRepository;

@Service
@RequiredArgsConstructor
public class CancelFlightUseCaseImpl implements CancelFlightUseCase {
    private final FlightRepository flightRepository;
    private final FlightStatusRepository flightStatusRepository;

    @Override
    public void cancelFlight(Long flightId) {
        FlightEntity flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new FlightNotFoundException(flightId));
        FlightStatusEntity cancelStatus = flightStatusRepository.findFlightStatusByName("CANCELLED")
                .orElseThrow(() -> new FlightStatusNotFoundException(flightId));
        flight.setStatus(cancelStatus);
        flightRepository.save(flight);
    }
}
