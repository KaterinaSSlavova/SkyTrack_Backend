package skytrack.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import skytrack.business.service.QrGenerator;
import skytrack.business.useCase.booking.*;
import skytrack.dto.booking.BookingResponse;
import skytrack.dto.booking.CreateBookingRequest;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final QrGenerator qrGenerator;
    private final CreateBookingUseCase createBookingUseCase;
    private final GetAllBookingsUseCase getAllBookingsUseCase;
    private final GetBookingUseCase getBookingUseCase;
    private final CancelBookingUseCase cancelBookingUseCase;
    private final GetBookingByReferenceUseCase getBookingByReferenceUseCase;

    @PreAuthorize("hasAnyRole('PASSENGER', 'ADMIN')")
    @GetMapping("{reference}/qr")
    public ResponseEntity<byte[]> getQRCode(@PathVariable("reference") final String reference) {
        byte[] qrBytes = qrGenerator.generate(reference);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(qrBytes);
    }

    @GetMapping("verify/{reference}")
    public ResponseEntity<BookingResponse> verifyBooking(@PathVariable String reference) {
        BookingResponse response = getBookingByReferenceUseCase.getBookingByReference(reference);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('PASSENGER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        List<BookingResponse> response = getAllBookingsUseCase.getAllBookings();
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasAnyRole('PASSENGER', 'ADMIN')")
    @GetMapping("{id}")
    public ResponseEntity<BookingResponse> getBooking(@PathVariable("id") final Long id) {
        BookingResponse response = getBookingUseCase.getBooking(id);
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasRole('PASSENGER')")
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@RequestBody @Valid CreateBookingRequest request){
        BookingResponse response = createBookingUseCase.toResponse(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('PASSENGER')")
    @PatchMapping("{id}/cancel")
    public ResponseEntity<BookingResponse> cancelBooking(@PathVariable("id") final Long id) {
        cancelBookingUseCase.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }
}
