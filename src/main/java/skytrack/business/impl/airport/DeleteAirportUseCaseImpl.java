package skytrack.business.impl.airport;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.exception.AirportNotFoundException;
import skytrack.business.repository.AirportRepository;
import skytrack.business.useCase.airport.DeleteAirportUseCase;

@Service
@RequiredArgsConstructor
public class DeleteAirportUseCaseImpl implements DeleteAirportUseCase {
    private final AirportRepository airportRepository;

    @Override
    public void deleteAirport(Long id) {
        validateAirport(id);
        airportRepository.deleteAirport(id);
    }

    private void validateAirport(Long id) {
        if(!airportRepository.existsById(id)) {
            throw new AirportNotFoundException(id);
        }
    }
}
