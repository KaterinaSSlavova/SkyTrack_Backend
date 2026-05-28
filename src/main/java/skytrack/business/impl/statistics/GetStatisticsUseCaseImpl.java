package skytrack.business.impl.statistics;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.service.UserService;
import skytrack.business.useCase.statistics.GetStatisticsUseCase;
import skytrack.dto.booking.BookingStatsResponse;
import skytrack.persistence.repo.BookingRepository;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class GetStatisticsUseCaseImpl implements GetStatisticsUseCase {
    private final BookingRepository bookingRepository;
    private final UserService userService;

    @Override
    public BookingStatsResponse getStatistics() {
        Long id = userService.getLoggedUser().getId();

        return new BookingStatsResponse(
                bookingRepository.countByUser_Id(id),
                bookingRepository.sumTotalPriceByUserId(id),
                bookingRepository.countDistinctDestinationsByUserId(id),
                bookingRepository.countUpcomingByUserId(id, Instant.now())
        );
    }
}
