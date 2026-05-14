package skytrack.business.impl.duffel;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.exception.flight.FlightNotFoundException;
import skytrack.business.mapper.FlightMapper;
import skytrack.business.useCase.flight.GetDuffelFlightUseCase;
import skytrack.dto.duffel.SavedFlightResponse;
import skytrack.persistence.repo.DuffelRepository;

@Service
@RequiredArgsConstructor
public class GetDuffelFlightUseCaseImpl implements GetDuffelFlightUseCase {
    private final DuffelRepository duffelRepository;
    private final FlightMapper flightMapper;

    @Override
    public SavedFlightResponse getDuffelFlightById(Long id) {
        return flightMapper.toResponse(duffelRepository.findById(id)
                .orElseThrow(() -> new FlightNotFoundException(id)));
    }
}
