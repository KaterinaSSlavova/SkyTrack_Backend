package skytrack.airport;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skytrack.business.exception.airport.AirportNotFoundException;
import skytrack.business.impl.airport.UpdateAirportUseCaseImpl;
import skytrack.business.service.AirportValidationService;
import skytrack.dto.airport.UpdateAirportRequest;
import skytrack.persistence.entity.AirportEntity;
import skytrack.persistence.repo.AirportRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateAirportUseCaseImplTest {
    @Mock
    private AirportRepository airportRepository;

    @Mock
    private AirportValidationService airportValidationService;

    @InjectMocks
    private UpdateAirportUseCaseImpl updateAirportUseCaseImpl;

    @Test
    public void updateAirport_shouldUpdateAirport_whenAirportExists() {
        //arrange
        UpdateAirportRequest request = new UpdateAirportRequest(1L, "AMS", "Schiphol", "Amsterdam", "Netherlands", "Europe/Amsterdam");;
        when(airportRepository.existsById(request.getId())).thenReturn(true);

        //act
        updateAirportUseCaseImpl.updateAirport(request);

        //assert
        verify(airportRepository).save(any(AirportEntity.class));
    }

    @Test
    public void updateAirport_shouldNotUpdateAirport_whenAirportDoesNotExist() {
        //arrange
        UpdateAirportRequest request = new UpdateAirportRequest(1L, "AMS", "Schiphol", "Amsterdam", "Netherlands", "Europe/Amsterdam");
        when(airportRepository.existsById(1L)).thenReturn(false);

        //act and assert
        assertThrows(AirportNotFoundException.class, () -> updateAirportUseCaseImpl.updateAirport(request));
        verify(airportRepository, never()).save(any(AirportEntity.class));
    }
}
