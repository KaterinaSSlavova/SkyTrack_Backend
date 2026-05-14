package skytrack.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import skytrack.business.useCase.seat.GetSeatMapUseCase;
import skytrack.business.useCase.seat.GetSeatUseCase;
import skytrack.dto.seat.SeatMapResponse;
import skytrack.dto.seat.SeatResponse;

@RestController
@RequestMapping("/seats")
@RequiredArgsConstructor
public class SeatController {

    private final GetSeatUseCase getSeatUseCase;
    private final GetSeatMapUseCase getSeatMapUseCase;

    @GetMapping("{seatId}/flight/{flightId}")
    public ResponseEntity<SeatResponse> getSeat(@PathVariable("seatId")final long seatId, @PathVariable("flightId")final long flightId){
        SeatResponse response = getSeatUseCase.getSeat(seatId, flightId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<SeatMapResponse> getSeatMap(@PathVariable("id")final long id){
        SeatMapResponse response = getSeatMapUseCase.getSeatMap(id);
        return ResponseEntity.ok(response);
    }
}
