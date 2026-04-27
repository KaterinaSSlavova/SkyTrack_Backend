package skytrack.business.impl.seat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.exception.flight.FlightNotFoundException;
import skytrack.business.exception.seat.SeatNotFoundException;
import skytrack.business.mapper.SeatMapper;
import skytrack.business.useCase.seat.GetSeatUseCase;
import skytrack.dto.seat.SeatResponse;
import skytrack.persistence.entity.SeatEntity;
import skytrack.persistence.repo.BookingRepository;
import skytrack.persistence.repo.DuffelRepository;
import skytrack.persistence.repo.SeatRepository;

@Service
@RequiredArgsConstructor
public class GetSeatUseCaseImpl implements GetSeatUseCase {
    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;
    private final DuffelRepository duffelRepository;
    private final SeatMapper seatMapper;

    @Override
    public SeatResponse getSeat(Long seatId, Long flightId) {
        SeatEntity seat = seatRepository.findById(seatId).orElseThrow(() -> new SeatNotFoundException(seatId));
        if(!duffelRepository.existsById(flightId)){
            throw new FlightNotFoundException(flightId);
        }
        boolean isAvailable = !bookingRepository.existsByExternalFlightIdAndSeatId(flightId, seatId);
        return seatMapper.toResponse(seat, isAvailable);
    }
}
