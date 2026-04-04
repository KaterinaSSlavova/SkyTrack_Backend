package skytrack.business.impl.airport;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.exception.AirportNotFoundException;
import skytrack.business.useCase.airport.ArchiveAirportUseCase;
import skytrack.persistence.entity.AirportEntity;
import skytrack.persistence.repo.AirportRepository;

@Service
@RequiredArgsConstructor
public class ArchiveAirportUseCaseImpl implements ArchiveAirportUseCase {
    private final AirportRepository airportRepository;

    @Override
    public void archiveAirport(Long id) {
        AirportEntity entity = airportRepository.findByIdAndIsArchivedFalse(id)
                .orElseThrow(() -> new AirportNotFoundException(id));
        entity.setIsArchived(true);
        airportRepository.save(entity);
    }
}
