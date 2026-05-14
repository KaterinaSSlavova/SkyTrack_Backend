package skytrack.business.impl.duffel;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.mapper.FlightMapper;
import skytrack.business.useCase.flight.GetAllDuffelFlightsUseCase;
import skytrack.dto.duffel.SavedFlightResponse;
import skytrack.persistence.repo.DuffelRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllDuffelFlightsUseCaseImpl implements GetAllDuffelFlightsUseCase {
    private final DuffelRepository duffelRepository;
    private final FlightMapper flightMapper;

    @Override
    public List<SavedFlightResponse> getAllDuffelFlights() {
        return duffelRepository.findAll().stream().map(flightMapper::toResponse).toList();
    }
}
