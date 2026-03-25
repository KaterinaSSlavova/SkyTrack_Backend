package skytrack.business.impl.flight;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.exception.FlightNotFoundException;
import skytrack.business.repository.FlightRepository;
import skytrack.business.useCase.flight.DeleteFlightUseCase;

@Service
@RequiredArgsConstructor
public class DeleteFlightUseCaseImpl implements DeleteFlightUseCase {
    private final FlightRepository flightRepository;

    @Override
    public void deleteFlight(Long flightId) {
        validateFlight(flightId);
        flightRepository.deleteFlight(flightId);
    }

    private void validateFlight(Long id){
        if(!flightRepository.existsById(id)){
            throw new FlightNotFoundException(id);
        }
    }
}
