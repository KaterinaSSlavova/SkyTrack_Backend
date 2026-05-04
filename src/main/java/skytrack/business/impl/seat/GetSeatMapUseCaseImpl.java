package skytrack.business.impl.seat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.exception.flight.FlightNotFoundException;
import skytrack.business.mapper.SeatMapper;
import skytrack.business.useCase.seat.GetSeatMapUseCase;
import skytrack.dto.seat.SeatMapResponse;
import skytrack.dto.seat.SeatResponse;
import skytrack.persistence.repo.BookingRepository;
import skytrack.persistence.repo.DuffelRepository;
import skytrack.persistence.repo.SeatRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetSeatMapUseCaseImpl implements GetSeatMapUseCase {
    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;
    private final DuffelRepository duffelRepository;
    private final SeatMapper seatMapper;

    @Override
    public SeatMapResponse getSeatMap(Long flightId) {
        if(!duffelRepository.existsById(flightId)) {
            throw new FlightNotFoundException(flightId);
        }

        Set<Long> bookedSeatIds = bookingRepository.findByExternalFlight_Id(flightId)
                .stream()
                .map(b -> b.getSeat().getId())
                .collect(Collectors.toSet());

        List<SeatResponse> seats = seatRepository.findAll()
                .stream()
                .map(seat -> seatMapper.toResponse(seat, !bookedSeatIds.contains(seat.getId())))
                .toList();

        return SeatMapResponse.builder().flight_id(flightId).seats(seats).build();
    }
}
