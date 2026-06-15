package skytrack.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skytrack.business.service.SeatMapGenerator;
import skytrack.persistence.entity.SeatEntity;
import skytrack.persistence.repo.SeatRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SeatMapGeneratorTest {

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private SeatMapGenerator seatMapGenerator;

    @Test
    void generateSeatMap_shouldGenerate180Seats_whenDatabaseIsEmpty() {
        // Arrange
        when(seatRepository.findAll()).thenReturn(List.of());

        // Act
        seatMapGenerator.generateSeatMap();

        // Assert
        ArgumentCaptor<List<SeatEntity>> captor = ArgumentCaptor.forClass(List.class);

        verify(seatRepository).saveAll(captor.capture());

        List<SeatEntity> seats = captor.getValue();

        assertEquals(180, seats.size());
    }

    @Test
    void generateSeatMap_shouldSkipExistingSeats() {
        // Arrange
        SeatEntity existingSeat = SeatEntity.builder()
                .seatNumber("1A")
                .build();

        when(seatRepository.findAll())
                .thenReturn(List.of(existingSeat));

        // Act
        seatMapGenerator.generateSeatMap();

        // Assert
        ArgumentCaptor<List<SeatEntity>> captor =
                ArgumentCaptor.forClass(List.class);

        verify(seatRepository).saveAll(captor.capture());

        List<SeatEntity> seats = captor.getValue();

        assertEquals(179, seats.size());

        boolean contains1A = seats.stream().anyMatch(s -> s.getSeatNumber().equals("1A"));

        assertFalse(contains1A);
    }

    @Test
    void generateSeatMap_shouldAssignCorrectSeatProperties() {
        // Arrange
        when(seatRepository.findAll()).thenReturn(List.of());

        // Act
        seatMapGenerator.generateSeatMap();

        // Assert
        ArgumentCaptor<List<SeatEntity>> captor =
                ArgumentCaptor.forClass(List.class);

        verify(seatRepository).saveAll(captor.capture());

        List<SeatEntity> seats = captor.getValue();

        SeatEntity seat1A = seats.stream()
                .filter(s -> s.getSeatNumber().equals("1A"))
                .findFirst()
                .orElseThrow();

        assertTrue(seat1A.getWindow());
        assertFalse(seat1A.getAisle());
        assertTrue(seat1A.getExtraLegroom());

        SeatEntity seat5C = seats.stream()
                .filter(s -> s.getSeatNumber().equals("5C"))
                .findFirst()
                .orElseThrow();

        assertFalse(seat5C.getWindow());
        assertTrue(seat5C.getAisle());
        assertFalse(seat5C.getExtraLegroom());
    }
}
