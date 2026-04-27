package skytrack.business.impl.airport;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.exception.airport.AirportAlreadyExistsException;
import skytrack.business.mapper.AirportMapper;
import skytrack.business.service.AirportValidationService;
import skytrack.business.useCase.airport.CreateAirportUseCase;
import skytrack.dto.airport.AirportResponse;
import skytrack.dto.airport.CreateAirportRequest;
import skytrack.persistence.entity.AirportEntity;
import skytrack.persistence.repo.AirportRepository;

@Service
@RequiredArgsConstructor
public class CreateAirportUseCaseImpl implements CreateAirportUseCase {
    private final AirportRepository airportRepository;
    private final AirportValidationService  airportValidationService;
    private final AirportMapper airportMapper;

    @Override
    public AirportResponse createAirport(CreateAirportRequest request) {
        if(airportRepository.existsByIataCode(request.getIataCode())){
            throw new AirportAlreadyExistsException(request.getIataCode());
        }
        AirportEntity airport = airportMapper.toEntity(request);
        airportValidationService.validateAirport(airport);
        return airportMapper.toResponse(airportRepository.save(airport));
    }
}
