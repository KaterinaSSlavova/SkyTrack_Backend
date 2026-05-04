package skytrack.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.persistence.entity.SeatEntity;
import skytrack.persistence.repo.SeatRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatMapGenerator {
    private final SeatRepository seatRepository;

    /**
     * Automatically generates A320 seat map when the program starts.
     * Creates a standard 3-3 layout (A B C | D E F).
     */
    public void generateSeatMap(){
        List<SeatEntity> existingSeats = seatRepository.findAll();
        List<SeatEntity> newSeats = new ArrayList<>();
        Set<String> existingSeatNumbers = existingSeats.stream()
                .map(SeatEntity::getSeatNumber)
                .collect(Collectors.toSet());

        for(int row=1; row<=30; row++){
           for(char letter='A';letter<='F';letter++){
               String seatNumber = row + String.valueOf(letter);

               if(existingSeatNumbers.contains(seatNumber)){
                   continue;
               }

               newSeats.add(SeatEntity.builder()
                       .seatNumber(seatNumber)
                       .window(letter == 'A' || letter == 'F')
                       .aisle(letter == 'C' || letter == 'D')
                       .extraLegroom(row == 1 || row == 12 || row == 13)
                       .build());
           }
       }
        seatRepository.saveAll(newSeats);
    }
}
