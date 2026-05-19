package skytrack.airport;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skytrack.business.exception.airport.AirportNotFoundException;
import skytrack.business.impl.airport.ArchiveAirportUseCaseImpl;
import skytrack.persistence.entity.AirportEntity;
import skytrack.persistence.repo.AirportRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ArchiveAirportUseCaseImplTest {
    @Mock
    private AirportRepository airportRepository;

    @InjectMocks
    private ArchiveAirportUseCaseImpl archiveAirportUseCaseImpl;

    @Test
     void archiveAirport_shouldArchiveAirport_whenAirportExists() {
        //arrange
        AirportEntity airport = new AirportEntity(1L, "AMS", "Schiphol", "Amsterdam", "Netherlands", "Europe/Amsterdam",false);
        when(airportRepository.findByIdAndIsArchivedFalse(airport.getId()))
                .thenReturn(Optional.of(airport));

        //act
        archiveAirportUseCaseImpl.archiveAirport(airport.getId());

        //assert
        assertTrue(airport.getIsArchived());
        verify(airportRepository).save(airport);
    }

    @Test
     void archiveAirport_shouldNotArchiveAirport_whenAirportDoNotExists() {
       //arrange
        Long id = 1L;
        when(airportRepository.findByIdAndIsArchivedFalse(id)).thenReturn(Optional.empty());

        //act and assert
        assertThrows(AirportNotFoundException.class, () -> archiveAirportUseCaseImpl.archiveAirport(id));
        verify(airportRepository, never()).save(any(AirportEntity.class));
    }
}
