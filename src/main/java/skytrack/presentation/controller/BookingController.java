package skytrack.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skytrack.business.service.QrGenerator;
import skytrack.business.useCase.booking.CancelBookingUseCase;
import skytrack.business.useCase.booking.CreateBookingUseCase;
import skytrack.business.useCase.booking.GetAllBookingsUseCase;
import skytrack.business.useCase.booking.GetBookingUseCase;
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

    @GetMapping("{reference}/qr")
    public ResponseEntity<byte[]> getQRCode(@PathVariable("reference") final String reference) {
        byte[] qrBytes = qrGenerator.generate(reference);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(qrBytes);
    }

    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        List<BookingResponse> response = getAllBookingsUseCase.getAllBookings();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<BookingResponse> getBooking(@PathVariable("id") final Long id) {
        BookingResponse response = getBookingUseCase.getBooking(id);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@RequestBody @Valid CreateBookingRequest request){
        BookingResponse response = createBookingUseCase.toResponse(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("{id}/cancel")
    public ResponseEntity<BookingResponse> cancelBooking(@PathVariable("id") final Long id) {
        cancelBookingUseCase.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }
}
