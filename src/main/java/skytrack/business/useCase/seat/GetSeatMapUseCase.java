package skytrack.business.useCase.seat;

import skytrack.dto.seat.SeatMapResponse;

public interface GetSeatMapUseCase {
    SeatMapResponse getSeatMap(Long id);
}
