package skytrack.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import skytrack.business.useCase.flight.*;
import skytrack.dto.duffel.DuffelFlightResponse;
import skytrack.dto.duffel.SavedFlightResponse;
import skytrack.persistence.enumeration.FlightStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/flights")
@RequiredArgsConstructor
public class FlightController {
    private final SearchDuffelFlightUseCase searchDuffelFlightUseCase;
    private final SaveDuffelUseCase saveDuffelUseCase;
    private final UpdateFlightStatusUseCase updateFlightStatusUseCase;
    private final UpdateFlightGateUseCase updateFlightGateUseCase;
    private final GetAllDuffelFlightsUseCase getAllDuffelFlightsUseCase;
    private final GetDuffelFlightUseCase getDuffelFlightUseCase;

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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/duffel")
    public ResponseEntity<List<SavedFlightResponse>> getAllDuffelFlights(){
        List<SavedFlightResponse> response  = getAllDuffelFlightsUseCase.getAllDuffelFlights();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/duffel/{id}")
    public ResponseEntity<SavedFlightResponse> getDuffelFlightById(@PathVariable("id")final long id){
        SavedFlightResponse response =  getDuffelFlightUseCase.getDuffelFlightById(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/duffel/{id}/gate")
    public ResponseEntity<Void> updateFlightGate(@PathVariable("id")final long id,
                                                 @RequestParam @Valid String gate){
        updateFlightGateUseCase.updateFlightGate(id, gate);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/duffel/{id}/status")
    public ResponseEntity<Void> updateFlightStatus(@PathVariable("id")final long id,
                                                   @RequestParam @Valid FlightStatus status,
                                                   @RequestParam(required = false) LocalDateTime newDepTime){
        updateFlightStatusUseCase.updateFlightStatus(id, status, newDepTime);
        return ResponseEntity.noContent().build();
    }
}