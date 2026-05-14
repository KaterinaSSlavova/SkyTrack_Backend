package skytrack.business.impl.duffel;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.exception.flight.FlightNotFoundException;
import skytrack.business.service.NotificationService;
import skytrack.business.service.TimeConverter;
import skytrack.business.useCase.flight.UpdateFlightStatusUseCase;
import skytrack.persistence.entity.DuffelFlightEntity;
import skytrack.persistence.enumeration.FlightStatus;
import skytrack.persistence.enumeration.NotificationType;
import skytrack.persistence.repo.BookingRepository;
import skytrack.persistence.repo.DuffelRepository;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UpdateFlightStatusUseCaseImpl implements UpdateFlightStatusUseCase {
    private final DuffelRepository duffelRepository;
    private final NotificationService notificationService;
    private final BookingRepository bookingRepository;
    private final TimeConverter timeConverter;

    @Override
    public void updateFlightStatus(Long id, FlightStatus status, LocalDateTime departureTime) {
        DuffelFlightEntity flight = duffelRepository.findById(id)
                .orElseThrow(() -> new FlightNotFoundException(id));

        flight.setStatus(status);

        if(status ==FlightStatus.DELAYED){
            Instant utcTime = timeConverter.convertToUTC(departureTime, flight.getDepartureTimezone());
            flight.setDepartureTime(utcTime);
        }
        duffelRepository.save(flight);

        NotificationType type = switch (status) {
            case DELAYED -> NotificationType.FLIGHT_DELAYED;
            case CANCELLED -> NotificationType.FLIGHT_CANCELLED;
            case SCHEDULED -> null;
        };

        if (type == null) return;

        bookingRepository.findByExternalFlight_Id(flight.getId())
                .forEach(booking -> {
                        if(status.equals(FlightStatus.CANCELLED)){
                            booking.setArchived(true);
                            bookingRepository.save(booking);
                        }

                        notificationService.createNotification(
                                type,
                                booking.getUser(),
                                flight
                        );
                });
    }
}
