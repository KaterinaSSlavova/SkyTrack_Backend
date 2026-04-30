package skytrack.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityReturnValueHandler;
import reactor.core.publisher.Mono;
import skytrack.business.useCase.flight.*;
import skytrack.dto.duffel.DuffelFlightResponse;
import skytrack.dto.duffel.SavedFlightResponse;
import skytrack.dto.flight.CreateFlightRequest;
import skytrack.dto.flight.FlightResponse;
import skytrack.dto.flight.GetAllFlightsResponse;
import skytrack.dto.flight.UpdateFlightRequest;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/flights")
@RequiredArgsConstructor
public class FlightController {
    private final CreateFlightUseCase createFlightUseCase;
    private final GetFlightUseCase getFlightUseCase;
    private final GetAllFlightsUseCase getAllFlightsUseCase;
    private final UpdateFlightUseCase updateFlightUseCase;
    private final CancelFlightUseCase cancelFlightUseCase;
    private final SearchFlightUseCase searchFlightUseCase;
    private final SearchDuffelFlightUseCase searchDuffelFlightUseCase;
    private final SaveDuffelUseCase saveDuffelUseCase;

    @PreAuthorize("hasAnyRole('PASSENGER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<GetAllFlightsResponse> getAllFlights(){
        return ResponseEntity.ok(getAllFlightsUseCase.getAllFlights());
    }

    @PreAuthorize("hasAnyRole('PASSENGER', 'ADMIN')")
    @GetMapping("{id}")
    public ResponseEntity<FlightResponse> getFlightById(@PathVariable("id")final Long id){
        return ResponseEntity.ok(getFlightUseCase.getFlightById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<FlightResponse> createFlight(@RequestBody @Valid CreateFlightRequest request){
        FlightResponse response = createFlightUseCase.createFlight(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<Void> updateFlight(@PathVariable("id")final Long id, @RequestBody @Valid UpdateFlightRequest request){
        request.setId(id);
        updateFlightUseCase.updateFlight(request);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("{id}/cancel")
    public ResponseEntity<Void> cancelFlight(@PathVariable("id") final Long id){
        cancelFlightUseCase.cancelFlight(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('PASSENGER')")
    @GetMapping("/search")
    public ResponseEntity<List<FlightResponse>> searchFlights
            (@RequestParam String departureIata, @RequestParam String arrivalIata, @RequestParam LocalDate departureDate){
        List<FlightResponse> response = searchFlightUseCase.searchFlights(departureIata, arrivalIata, departureDate);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('PASSENGER')")
    @GetMapping("/duffel/search")
    public List<DuffelFlightResponse> duffelSearchFlights
            (@RequestParam String departureIata,
             @RequestParam String arrivalIata,
             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate){
        return searchDuffelFlightUseCase.searchFlights(departureIata, arrivalIata, departureDate).block();
    }

    @PreAuthorize("hasRole('PASSENGER')")
    @PostMapping("/duffel/save")
    public ResponseEntity<SavedFlightResponse> createFlight(@RequestBody @Valid DuffelFlightResponse response){
         SavedFlightResponse flightResponse = saveDuffelUseCase.saveFlight(response);
         return ResponseEntity.status(HttpStatus.CREATED).body(flightResponse);
    }
}