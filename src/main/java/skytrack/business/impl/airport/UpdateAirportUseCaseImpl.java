package skytrack.business.impl.airport;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.exception.AirportNotFoundException;
import skytrack.business.mapper.AirportMapper;
import skytrack.business.repository.AirportRepository;
import skytrack.business.useCase.airport.UpdateAirportUseCase;
import skytrack.domain.entity.Airport;
import skytrack.dto.airport.UpdateAirportRequest;

@Service
@RequiredArgsConstructor
public class UpdateAirportUseCaseImpl implements UpdateAirportUseCase {
    private final AirportRepository airportRepository;

    @Override
    public void updateAirport(UpdateAirportRequest request) {
        validateAirport(request.getId());
        Airport updatedAirport = AirportMapper.toDomain(request);
        airportRepository.updateAirport(updatedAirport);
    }

    private void validateAirport(Long id) {
        if(!airportRepository.existsById(id)){
            throw new AirportNotFoundException(id);
        }
    }
}
