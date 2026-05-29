package skytrack.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import skytrack.business.useCase.service.PassengerValidation;
import skytrack.dto.user.PassengerRequest;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/passengers")
public class PassengerController {
    private final PassengerValidation passengerValidation;

    @PreAuthorize("hasRole('PASSENGER')")
    @PostMapping("/validate")
    public ResponseEntity<Void> validatePassenger(@RequestBody @Valid PassengerRequest request,
                                                  @RequestParam LocalDate departureDate) {
        passengerValidation.validateAge(request.getDateOfBirth());
        passengerValidation.validatePassportNumber(request.getPassportNumber());
        passengerValidation.validatePassportExpiration(request.getPassportExpiry(), departureDate);
        return ResponseEntity.noContent().build();
    }
}