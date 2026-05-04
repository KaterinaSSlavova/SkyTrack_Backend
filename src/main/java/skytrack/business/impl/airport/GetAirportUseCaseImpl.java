package skytrack.business.impl.airport;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.exception.airport.AirportNotFoundException;
import skytrack.business.mapper.AirportMapper;
import skytrack.business.useCase.airport.GetAirportUseCase;
import skytrack.dto.airport.AirportResponse;
import skytrack.persistence.entity.AirportEntity;
import skytrack.persistence.repo.AirportRepository;

@Service
@RequiredArgsConstructor
public class GetAirportUseCaseImpl implements GetAirportUseCase {
    private final AirportRepository airportRepository;
    private final AirportMapper airportMapper;

    @Override
    public AirportResponse getAirportById(Long id) {
        AirportEntity airport = airportRepository.findByIdAndIsArchivedFalse(id)
                .orElseThrow(() -> new AirportNotFoundException(id));
        return airportMapper.toResponse(airport);
    }
}
