package skytrack.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import skytrack.business.service.SeatMapGenerator;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeatMapSeeder implements CommandLineRunner {
    private final SeatMapGenerator seatMapGenerator;

    @Override
    public void run(String... args){
        log.info("Generating seat map...");
        seatMapGenerator.generateSeatMap();
        log.info("Seat map generated successfully.");
    }
}
