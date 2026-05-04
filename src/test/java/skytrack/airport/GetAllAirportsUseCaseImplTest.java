package skytrack.airport;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skytrack.business.impl.airport.GetAllAirportsUseCaseImpl;
import skytrack.business.mapper.AirportMapper;
import skytrack.dto.airport.AirportResponse;
import skytrack.dto.airport.GetAllAirports;
import skytrack.persistence.entity.AirportEntity;
import skytrack.persistence.repo.AirportRepository;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetAllAirportsUseCaseImplTest {
    @Mock
    private AirportRepository airportRepository;

    @Mock
    private AirportMapper airportMapper;

    @InjectMocks
    private GetAllAirportsUseCaseImpl getAllAirportsUseCaseImpl;

    @Test
    void getAllAirports_shouldReturnAirports_whenAirportsExist() {
        //arrange
        List<AirportEntity> airports = List.of(
                new AirportEntity(1L, "AMS", "Schiphol", "Amsterdam", "Netherlands", "Europe/Amsterdam", false),
                new AirportEntity(2L, "ATH", "Athens Airport", "Athens", "Greece", "Europe/Athens", false)
        );

        AirportResponse response1 = new AirportResponse(1L, "AMS", "Schiphol", "Amsterdam", "Netherlands", "Europe/Amsterdam");
        AirportResponse response2 = new AirportResponse(2L, "ATH", "Athens Airport", "Athens", "Greece", "Europe/Athens");
        when(airportRepository.findByIsArchivedFalse()).thenReturn(airports);
        when(airportMapper.toResponse(airports.getFirst())).thenReturn(response1);
        when(airportMapper.toResponse(airports.getLast())).thenReturn(response2);

        //act
        GetAllAirports response = getAllAirportsUseCaseImpl.getAllAirports();

        //assert
        assertNotNull(response);
        assertEquals(response.getAirports().size(), airports.size());
    }

    @Test
    public void getAllAirports_shouldReturnEmptyList_whenNoAirportsExist() {
        //arrange
        when(airportRepository.findByIsArchivedFalse()).thenReturn(List.of());

        //act
        GetAllAirports response = getAllAirportsUseCaseImpl.getAllAirports();

        //assert
        assertTrue(response.getAirports().isEmpty());
        verify(airportRepository).findByIsArchivedFalse();

    }
}
