package skytrack.business.impl.airport;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.mapper.AirportMapper;
import skytrack.business.useCase.airport.SearchAirportUseCase;
import skytrack.dto.airport.AirportResponse;
import skytrack.persistence.entity.AirportEntity;
import skytrack.persistence.repo.AirportRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class SearchAirportUseCaseImpl implements SearchAirportUseCase {
    private AirportRepository airportRepository;

    @Override
    public List<AirportResponse> searchAirport(String input) {
        List<AirportEntity> airports = airportRepository.searchAirports(input);
        return airports.stream().map(AirportMapper::toResponse).toList();
    }
}
