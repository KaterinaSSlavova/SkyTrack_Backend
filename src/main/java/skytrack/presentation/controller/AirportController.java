package skytrack.presentation.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import skytrack.business.useCase.airport.*;
import skytrack.dto.airport.AirportResponse;
import skytrack.dto.airport.CreateAirportRequest;
import skytrack.dto.airport.GetAllAirports;
import skytrack.dto.airport.UpdateAirportRequest;

import java.util.List;

@RestController
@RequestMapping("/airports")
@AllArgsConstructor
public class AirportController {
    private final GetAllAirportsUseCase getAllAirportsUseCase;
    private final GetAirportUseCase getAirportUseCase;
    private final CreateAirportUseCase createAirportUseCase;
    private final UpdateAirportUseCase updateAirportUseCase;
    private final ArchiveAirportUseCase archiveAirportUseCase;
    private final SearchAirportUseCase searchAirportUseCase;

    @PreAuthorize("hasAnyRole('PASSENGER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<GetAllAirports> getAllAirports() {
        GetAllAirports response = getAllAirportsUseCase.getAllAirports();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('PASSENGER', 'ADMIN')")
    @GetMapping("{id}")
    public ResponseEntity<AirportResponse> getAirportById(@PathVariable("id")final Long id) {
        AirportResponse response = getAirportUseCase.getAirportById(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<AirportResponse> createAirport(@RequestBody @Valid CreateAirportRequest request) {
        AirportResponse response = createAirportUseCase.createAirport(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<Void> updateAirport(@PathVariable("id")final Long id,
                                                         @RequestBody @Valid UpdateAirportRequest request) {
        request.setId(id);
        updateAirportUseCase.updateAirport(request);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("{id}")
    public ResponseEntity<Void> archiveAirport(@PathVariable("id")final Long id) {
        archiveAirportUseCase.archiveAirport(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('PASSENGER')")
    @GetMapping("/search")
    public ResponseEntity<List<AirportResponse>> searchAirports(@RequestParam String input){
        List<AirportResponse> response = searchAirportUseCase.searchAirport(input);
        return ResponseEntity.ok(response);
    }
}
