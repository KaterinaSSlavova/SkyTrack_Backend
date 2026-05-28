package skytrack.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import skytrack.business.useCase.service.PassengerValidation;
import skytrack.dto.user.PassengerRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/passengers")
public class PassengerController {
    private final PassengerValidation passengerValidation;

    @PreAuthorize("hasRole('PASSENGER')")
    @PostMapping("/validate")
    public ResponseEntity<Void> validatePassenger(@RequestBody @Valid PassengerRequest request) {
        passengerValidation.validateAge(request.getDateOfBirth());
        passengerValidation.validatePassportNumber(request.getPassportNumber());
        return ResponseEntity.noContent().build();
    }
}