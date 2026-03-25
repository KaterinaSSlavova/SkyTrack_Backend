package skytrack.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skytrack.business.useCase.airport.*;
import skytrack.dto.airport.AirportResponse;
import skytrack.dto.airport.CreateAirportRequest;
import skytrack.dto.airport.GetAllAirports;
import skytrack.dto.airport.UpdateAirportRequest;

@RestController
@RequestMapping("/airports")
@AllArgsConstructor
public class AirportController {
    private final GetAllAirportsUseCase getAllAirportsUseCase;
    private final GetAirportUseCase getAirportUseCase;
    private final CreateAirportUseCase createAirportUseCase;
    private final UpdateAirportUseCase updateAirportUseCase;
    private final DeleteAirportUseCase deleteAirportUseCase;

    @GetMapping
    public ResponseEntity<GetAllAirports> getAllAirports() {
        GetAllAirports response = getAllAirportsUseCase.getAllAirports();
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<AirportResponse> getAirportById(@PathVariable("id")final Long id) {
        AirportResponse response = getAirportUseCase.getAirportById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<AirportResponse> createAirport(@RequestBody @Valid CreateAirportRequest request) {
        AirportResponse response = createAirportUseCase.createAirport(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updateAirport(@PathVariable("id")final Long id,
                                                         @RequestBody @Valid UpdateAirportRequest request) {
        request.setId(id);
        updateAirportUseCase.updateAirport(request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteAirport(@PathVariable("id")final Long id) {
        deleteAirportUseCase.deleteAirport(id);
        return ResponseEntity.noContent().build();
    }
}
