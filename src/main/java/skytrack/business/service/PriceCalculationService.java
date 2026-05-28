package skytrack.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.persistence.entity.SeatEntity;
import skytrack.persistence.repo.ExtrasRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PriceCalculationService {
    private final ExtrasRepository extrasRepository;

    public BigDecimal calculate(SeatEntity seat, BigDecimal basePrice){
        BigDecimal total = basePrice;

        if(seat.getExtraLegroom()){
            BigDecimal price = extrasRepository.findByName("extra_legroom").getPrice();
            total = total.add(price);
        }

        if(seat.getWindow()){
            BigDecimal price = extrasRepository.findByName("window").getPrice();
            total = total.add(price);
        }
        return total;
    }
}
