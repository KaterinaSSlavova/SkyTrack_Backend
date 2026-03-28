package skytrack.airport;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skytrack.business.exception.AirportNotFoundException;
import skytrack.business.impl.airport.UpdateAirportUseCaseImpl;
import skytrack.business.repository.AirportRepository;

import skytrack.domain.entity.Airport;
import skytrack.dto.airport.UpdateAirportRequest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateAirportUseCaseImplTest {
    @Mock
    private AirportRepository airportRepository;

    @InjectMocks
    private UpdateAirportUseCaseImpl updateAirportUseCaseImpl;

    @Test
    public void updateAirport_shouldUpdateAirport_whenAirportExists() {
        //arrange
        UpdateAirportRequest request = new UpdateAirportRequest(1L, "AMS", "Schiphol", "Amsterdam", "Netherlands", "Europe/Amsterdam");
        when(airportRepository.existsById(request.getId())).thenReturn(true);

        //act
        updateAirportUseCaseImpl.updateAirport(request);

        //assert
        verify(airportRepository).updateAirport(any(Airport.class));
    }

    @Test
    public void updateAirport_shouldNotUpdateAirport_whenAirportDoesNotExist() {
        //arrange
        UpdateAirportRequest request = new UpdateAirportRequest(1L, "AMS", "Schiphol", "Amsterdam", "Netherlands", "Europe/Amsterdam");
        when(airportRepository.existsById(1L)).thenReturn(false);

        //act and assert
        assertThrows(AirportNotFoundException.class, () -> updateAirportUseCaseImpl.updateAirport(request));
        verify(airportRepository, never()).updateAirport(any(Airport.class));
    }
}
