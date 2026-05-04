package skytrack.airport;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skytrack.business.exception.airport.AirportAlreadyExistsException;
import skytrack.business.impl.airport.CreateAirportUseCaseImpl;
import skytrack.business.mapper.AirportMapper;
import skytrack.business.service.AirportValidationService;
import skytrack.dto.airport.AirportResponse;
import skytrack.dto.airport.CreateAirportRequest;
import skytrack.persistence.entity.AirportEntity;
import skytrack.persistence.repo.AirportRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateAirportUseCaseImplTest {
    @Mock
    private AirportRepository airportRepository;

    @Mock
    private AirportValidationService airportValidationService;

    @Mock
    private AirportMapper airportMapper;

    @InjectMocks
    private CreateAirportUseCaseImpl createAirportUseCaseImpl;

    @Test
    public void createAirport_shouldReturnAirportResponse_whenRequestIsNotNull() {
        //arrange
        AirportEntity airport = new AirportEntity(1L, "AMS", "Schiphol", "Amsterdam", "Netherlands", "Europe/Amsterdam", false);
        AirportResponse expectedAirport = new AirportResponse(1L, "AMS", "Schiphol", "Amsterdam", "Netherlands", "Europe/Amsterdam");
        CreateAirportRequest request = new CreateAirportRequest("AMS", "Schiphol", "Amsterdam", "Netherlands", "Europe/Amsterdam");
        when(airportRepository.existsByIataCode(request.getIataCode())).thenReturn(false);
        when(airportMapper.toEntity(request)).thenReturn(airport);
        when(airportMapper.toResponse(airport)).thenReturn(expectedAirport);
        when(airportRepository.save(any(AirportEntity.class))).thenReturn(airport);

        //act
        AirportResponse response = createAirportUseCaseImpl.createAirport(request);

        //assert
        assertNotNull(response);
        assertEquals(airport.getIataCode(), response.getIataCode());
        assertEquals(airport.getName(), response.getName());
        assertEquals(airport.getCity(), response.getCity());
        assertEquals(airport.getCountry(), response.getCountry());
        assertEquals(airport.getTimezone(), response.getTimezone());
        verify(airportValidationService).validateAirport(airport);
    }

    @Test
    public void createAirport_shouldThrowAirportAlreadyExistsException_whenIataCodeExists() {
        //arrange
        CreateAirportRequest request = new CreateAirportRequest("AMS", "Schiphol", "Amsterdam", "Netherlands", "Europe/Amsterdam");
        when(airportRepository.existsByIataCode(any(String.class))).thenReturn(true);

        //act and assert
        assertThrows(AirportAlreadyExistsException.class, () -> createAirportUseCaseImpl.createAirport(request));
        verify(airportRepository, never()).save(any(AirportEntity.class));
    }
}
