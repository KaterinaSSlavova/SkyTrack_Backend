package skytrack.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skytrack.business.useCase.flight.*;
import skytrack.dto.flight.CreateFlightRequest;
import skytrack.dto.flight.FlightResponse;
import skytrack.dto.flight.GetAllFlightsResponse;
import skytrack.dto.flight.UpdateFlightRequest;

@RestController
@RequestMapping("/flights")
@AllArgsConstructor
public class FlightController {
    private final CreateFlightUseCase createFlightUseCase;
    private final GetFlightUseCase getFlightUseCase;
    private final GetAllFlightsUseCase getAllFlightsUseCase;
    private final UpdateFlightUseCase updateFlightUseCase;
    private final DeleteFlightUseCase deleteFlightUseCase;

    @GetMapping
    public ResponseEntity<GetAllFlightsResponse> getAllFlights(){
        return ResponseEntity.ok(getAllFlightsUseCase.getAllFlights());
    }

    @GetMapping("{id}")
    public ResponseEntity<FlightResponse> getFlightById(@PathVariable("id")final Long id){
        return ResponseEntity.ok(getFlightUseCase.getFlightById(id));
    }

    @PostMapping
    public ResponseEntity<FlightResponse> createFlight(@RequestBody @Valid CreateFlightRequest request){
        FlightResponse response = createFlightUseCase.createFlight(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updateFlight(@PathVariable("id")final Long id, @RequestBody @Valid UpdateFlightRequest request){
        request.setId(id);
        updateFlightUseCase.updateFlight(request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable("id")final Long id){
        deleteFlightUseCase.deleteFlight(id);
        return ResponseEntity.noContent().build();
    }
}