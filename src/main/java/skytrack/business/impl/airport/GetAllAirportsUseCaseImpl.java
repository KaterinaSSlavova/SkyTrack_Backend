package skytrack.business.impl.airport;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.mapper.AirportMapper;
import skytrack.business.repository.AirportRepository;
import skytrack.business.useCase.airport.GetAllAirportsUseCase;
import skytrack.domain.entity.Airport;
import skytrack.dto.airport.AirportResponse;
import skytrack.dto.airport.GetAllAirports;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllAirportsUseCaseImpl implements GetAllAirportsUseCase {
    private final AirportRepository airportRepository;

    @Override
    public GetAllAirports getAllAirports() {
        List<Airport> airports = airportRepository.getAllAirports();
        List<AirportResponse> response = airports.stream().map(AirportMapper::toResponse).toList();
        return new GetAllAirports(response);
    }
}
