package skytrack.airport;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skytrack.business.exception.AirportNotFoundException;
import skytrack.business.impl.airport.ArchiveAirportUseCaseImpl;
import skytrack.business.repository.AirportRepository;

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
    public void archiveAirport_shouldArchiveAirport_whenAirportExists() {
        //arrange
        Long id = 1L;
        when(airportRepository.existsById(id)).thenReturn(true);

        //act
        archiveAirportUseCaseImpl.archiveAirport(id);

        //assert
        verify(airportRepository).deleteAirport(id);
    }

    @Test
    public void archiveAirport_shouldNotArchiveAirport_whenAirportDoNotExists() {
       //arrange
        Long id = 1L;
        when(airportRepository.existsById(id)).thenReturn(false);

        //act and assert
        assertThrows(AirportNotFoundException.class, () -> archiveAirportUseCaseImpl.archiveAirport(id));
        verify(airportRepository, never()).deleteAirport(id);
    }
}
