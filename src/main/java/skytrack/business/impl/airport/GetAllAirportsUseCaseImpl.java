package skytrack.business.impl.airport;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.mapper.AirportMapper;
import skytrack.business.useCase.airport.GetAllAirportsUseCase;
import skytrack.dto.airport.AirportResponse;
import skytrack.dto.airport.GetAllAirports;
import skytrack.persistence.entity.AirportEntity;
import skytrack.persistence.repo.AirportRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllAirportsUseCaseImpl implements GetAllAirportsUseCase {
    private final AirportRepository airportRepository;

    @Override
    public GetAllAirports getAllAirports() {
        List<AirportEntity> airports = airportRepository.findByIsArchivedFalse();
        List<AirportResponse> response = airports.stream().map(AirportMapper::toResponse).toList();
        return new GetAllAirports(response);
    }
}
