package skytrack.business.impl.airport;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.exception.AirportNotFoundException;
import skytrack.business.mapper.AirportMapper;
import skytrack.business.repository.AirportRepository;
import skytrack.business.useCase.airport.GetAirportUseCase;
import skytrack.domain.entity.Airport;
import skytrack.dto.airport.AirportResponse;

@Service
@RequiredArgsConstructor
public class GetAirportUseCaseImpl implements GetAirportUseCase {
    private final AirportRepository airportRepository;

    @Override
    public AirportResponse getAirportById(Long id) {
        Airport airport = airportRepository.getAirportById(id).orElseThrow(() -> new AirportNotFoundException(id));
        return AirportMapper.toResponse(airport);
    }
}
