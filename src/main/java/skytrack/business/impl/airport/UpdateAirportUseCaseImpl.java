package skytrack.business.impl.airport;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.exception.airport.AirportNotFoundException;
import skytrack.business.mapper.AirportMapper;
import skytrack.business.service.AirportValidationService;
import skytrack.business.useCase.airport.UpdateAirportUseCase;
import skytrack.dto.airport.UpdateAirportRequest;
import skytrack.persistence.entity.AirportEntity;
import skytrack.persistence.repo.AirportRepository;

@Service
@RequiredArgsConstructor
public class UpdateAirportUseCaseImpl implements UpdateAirportUseCase {
    private final AirportRepository airportRepository;
    private final AirportValidationService airportValidationService;

    @Override
    public void updateAirport(UpdateAirportRequest request) {
        validateAirportExists(request.getId());
        AirportEntity updatedAirport = AirportMapper.toEntity(request);
        airportValidationService.validateAirport(updatedAirport);
        airportRepository.save(updatedAirport);
    }

    private void validateAirportExists(Long id) {
        if(!airportRepository.existsById(id)){
            throw new AirportNotFoundException(id);
        }
    }
}
