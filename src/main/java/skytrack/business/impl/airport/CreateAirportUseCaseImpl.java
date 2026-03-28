package skytrack.business.impl.airport;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.exception.AirportNullException;
import skytrack.business.mapper.AirportMapper;
import skytrack.business.repository.AirportRepository;
import skytrack.business.useCase.airport.CreateAirportUseCase;
import skytrack.domain.entity.Airport;
import skytrack.dto.airport.AirportResponse;
import skytrack.dto.airport.CreateAirportRequest;

@Service
@RequiredArgsConstructor
public class CreateAirportUseCaseImpl implements CreateAirportUseCase {
    private final AirportRepository airportRepository;

    @Override
    public AirportResponse createAirport(CreateAirportRequest request) {
        if(request == null){
            throw new AirportNullException();
        }
        Airport airport = AirportMapper.toDomain(request);
        return AirportMapper.toResponse(airportRepository.saveAirport(airport));
    }
}
