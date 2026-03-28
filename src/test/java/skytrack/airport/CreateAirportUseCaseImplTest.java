package skytrack.airport;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skytrack.business.exception.AirportNullException;
import skytrack.business.impl.airport.CreateAirportUseCaseImpl;
import skytrack.business.repository.AirportRepository;
import skytrack.domain.entity.Airport;
import skytrack.dto.airport.AirportResponse;
import skytrack.dto.airport.CreateAirportRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateAirportUseCaseImplTest {
    @Mock
    private AirportRepository airportRepository;

    @InjectMocks
    private CreateAirportUseCaseImpl createAirportUseCaseImpl;

    @Test
    public void createFlight_shouldReturnAirportResponse_whenRequestIsNotNull() {
        //arrange
        Airport airport = new Airport(1L, "AMS", "Schiphol", "Amsterdam", "Netherlands", "Europe/Amsterdam");
        CreateAirportRequest request = new CreateAirportRequest("AMS", "Schiphol", "Amsterdam", "Netherlands", "Europe/Amsterdam");
        when(airportRepository.saveAirport(any(Airport.class))).thenReturn(airport);

        //act
        AirportResponse response = createAirportUseCaseImpl.createAirport(request);

        //assert
        assertNotNull(response);
        assertEquals(airport.getIataCode(), response.getIataCode());
        assertEquals(airport.getName(), response.getName());
        assertEquals(airport.getCity(), response.getCity());
        assertEquals(airport.getCountry(), response.getCountry());
        assertEquals(airport.getTimezone(), response.getTimezone());
    }

    @Test
    public void createFlight_shouldThrowAirportNullException_whenRequestIsNull() {
        //arrange
        CreateAirportRequest request = null;

        //act and assert
        assertThrows(AirportNullException.class, () -> createAirportUseCaseImpl.createAirport(request));
        verify(airportRepository, never()).saveAirport(any(Airport.class));
    }
}
