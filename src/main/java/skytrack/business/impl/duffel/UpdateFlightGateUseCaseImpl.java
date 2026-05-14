package skytrack.business.impl.duffel;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.exception.flight.FlightNotFoundException;
import skytrack.business.service.NotificationService;
import skytrack.business.useCase.flight.UpdateFlightGateUseCase;
import skytrack.persistence.entity.DuffelFlightEntity;
import skytrack.persistence.enumeration.NotificationType;
import skytrack.persistence.repo.BookingRepository;
import skytrack.persistence.repo.DuffelRepository;

@Service
@RequiredArgsConstructor
public class UpdateFlightGateUseCaseImpl implements UpdateFlightGateUseCase {
    private final DuffelRepository duffelRepository;
    private final BookingRepository bookingRepository;
    private final NotificationService notificationService;

    @Override
    public void updateFlightGate(Long id, String gate) {
        DuffelFlightEntity flight = duffelRepository.findById(id)
                .orElseThrow(() -> new FlightNotFoundException(id));
        flight.setGate(gate);
        duffelRepository.save(flight);

        bookingRepository.findByExternalFlight_Id(flight.getId())
                .forEach(booking -> notificationService.createNotification
                        (NotificationType.GATE_CHANGED, booking.getUser(), flight));
    }
}