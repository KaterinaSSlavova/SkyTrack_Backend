package skytrack.airport;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skytrack.business.exception.airport.AirportNotFoundException;
import skytrack.business.impl.airport.GetAirportUseCaseImpl;
import skytrack.business.mapper.AirportMapper;
import skytrack.dto.airport.AirportResponse;
import skytrack.persistence.entity.AirportEntity;
import skytrack.persistence.repo.AirportRepository;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetAirportUseCaseImplTest {
    @Mock
    private AirportRepository airportRepository;

    @Mock
    private AirportMapper airportMapper;

    @InjectMocks
    private GetAirportUseCaseImpl getAirportUseCaseImpl;

    @Test
    void getAirportById_shouldReturnAirport_whenAirportExists() {
        //arrange
        AirportEntity airport = new AirportEntity(1L, "AMS", "Schiphol", "Amsterdam", "Netherlands", "Europe/Amsterdam", false);
        AirportResponse expectedResponse = new AirportResponse(1L, "AMS", "Schiphol", "Amsterdam", "Netherlands", "Europe/Amsterdam");
        when(airportRepository.findByIdAndIsArchivedFalse(airport.getId())).thenReturn(Optional.of(airport));
        when(airportMapper.toResponse(airport)).thenReturn(expectedResponse);

        //act
        AirportResponse response = getAirportUseCaseImpl.getAirportById(airport.getId());

        //assert
        assertNotNull(response);
        assertEquals(airport.getId(), response.getId());
        assertEquals(airport.getIataCode(), response.getIataCode());
        assertEquals(airport.getName(), response.getName());
        assertEquals(airport.getCity(), response.getCity());
        assertEquals(airport.getCountry(), response.getCountry());
        assertEquals(airport.getTimezone(), response.getTimezone());
    }

    @Test
     void getAirportById_shouldThrowAirportNotFound_whenAirportDoesNotExist() {
        //assert
        Long id = 1L;
        when(airportRepository.findByIdAndIsArchivedFalse(id)).thenReturn(Optional.empty());

        //act and arrange
        assertThrows(AirportNotFoundException.class, () -> getAirportUseCaseImpl.getAirportById(id));
        verify(airportRepository).findByIdAndIsArchivedFalse(id);
    }
}
