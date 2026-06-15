package skytrack.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skytrack.business.service.PriceCalculationService;
import skytrack.persistence.entity.ExtrasEntity;
import skytrack.persistence.entity.SeatEntity;
import skytrack.persistence.repo.ExtrasRepository;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PriceCalculationServiceTest {
    @Mock
    private ExtrasRepository extrasRepository;

    @InjectMocks
    private PriceCalculationService priceCalculationService;

    @Test
    void calculate_shouldReturnBasePrice_whenSeatHasNoExtras() {
        // Arrange
        SeatEntity seat = SeatEntity.builder()
                .window(false)
                .extraLegroom(false)
                .build();

        BigDecimal basePrice = BigDecimal.valueOf(100);

        // Act
        BigDecimal result = priceCalculationService.calculate(seat, basePrice);

        // Assert
        assertEquals(BigDecimal.valueOf(100), result);
        verifyNoInteractions(extrasRepository);
    }

    @Test
    void calculate_shouldAddWindowPrice_whenSeatIsWindow() {
        // Arrange
        SeatEntity seat = SeatEntity.builder()
                .window(true)
                .extraLegroom(false)
                .build();

        ExtrasEntity windowExtra = ExtrasEntity.builder()
                .price(BigDecimal.valueOf(15))
                .build();

        when(extrasRepository.findByName("window"))
                .thenReturn(windowExtra);

        // Act
        BigDecimal result =
                priceCalculationService.calculate(seat, BigDecimal.valueOf(100));

        // Assert
        assertEquals(BigDecimal.valueOf(115), result);
        verify(extrasRepository).findByName("window");
    }

    @Test
    void calculate_shouldAddExtraLegroomPrice_whenSeatHasExtraLegroom() {
        // Arrange
        SeatEntity seat = SeatEntity.builder()
                .window(false)
                .extraLegroom(true)
                .build();

        ExtrasEntity legroomExtra = ExtrasEntity.builder()
                .price(BigDecimal.valueOf(20))
                .build();

        when(extrasRepository.findByName("extra_legroom"))
                .thenReturn(legroomExtra);

        // Act
        BigDecimal result =
                priceCalculationService.calculate(seat, BigDecimal.valueOf(100));

        // Assert
        assertEquals(BigDecimal.valueOf(120), result);
        verify(extrasRepository).findByName("extra_legroom");
    }

    @Test
    void calculate_shouldAddAllExtras_whenSeatHasWindowAndExtraLegroom() {
        // Arrange
        SeatEntity seat = SeatEntity.builder()
                .window(true)
                .extraLegroom(true)
                .build();

        ExtrasEntity windowExtra = ExtrasEntity.builder()
                .price(BigDecimal.valueOf(15))
                .build();

        ExtrasEntity legroomExtra = ExtrasEntity.builder()
                .price(BigDecimal.valueOf(20))
                .build();

        when(extrasRepository.findByName("window"))
                .thenReturn(windowExtra);

        when(extrasRepository.findByName("extra_legroom"))
                .thenReturn(legroomExtra);

        // Act
        BigDecimal result =
                priceCalculationService.calculate(seat, BigDecimal.valueOf(100));

        // Assert
        assertEquals(BigDecimal.valueOf(135), result);

        verify(extrasRepository).findByName("window");
        verify(extrasRepository).findByName("extra_legroom");
    }
}
