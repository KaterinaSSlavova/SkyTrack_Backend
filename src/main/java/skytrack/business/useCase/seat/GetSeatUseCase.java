package skytrack.business.useCase.seat;

import skytrack.dto.seat.SeatResponse;

public interface GetSeatUseCase {
    SeatResponse getSeat(Long seatId, Long flightId);
}
