package skytrack.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.exception.country.InvalidCountryException;
import skytrack.dto.country.VisitedCountryResponse;
import skytrack.persistence.entity.BookingEntity;
import skytrack.persistence.entity.UserEntity;
import skytrack.persistence.entity.VisitedCountryEntity;
import skytrack.persistence.repo.BookingRepository;
import skytrack.persistence.repo.VisitedCountryRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TravelSuggestionService {
    private final UserService userService;
    private final VisitedCountryRepository visitedCountryRepository;
    private final BookingRepository bookingRepository;

    public List<VisitedCountryResponse> getSuggestions() {
        UserEntity user = userService.getLoggedUser();
        List<BookingEntity> bookings = bookingRepository.findByUser_Id(user.getId());
        List<VisitedCountryEntity> visited = visitedCountryRepository.findByUserId(user.getId());

        Set<String> visitedCountriesCodes = visited.stream()
                .map(VisitedCountryEntity::getCountryCode).collect(Collectors.toSet());

        return bookings.stream()
                .map(b -> b.getExternalFlight().getArrivalAirport().getCountry())
                .distinct().map(countryName -> VisitedCountryResponse.builder()
                        .countryName(countryName)
                        .countryCode(getCountryCodeFromName(countryName))
                        .build())
                .filter(suggestion -> !visitedCountriesCodes.contains(suggestion.getCountryCode()))
                .toList();
    }

    private String getCountryCodeFromName(String countryName){
        return Arrays.stream(Locale.getISOCountries())
                .filter(code -> new Locale("", code)
                        .getDisplayCountry(Locale.ENGLISH).equalsIgnoreCase(countryName))
                .findFirst().orElseThrow(() -> new InvalidCountryException(countryName));
    }
}
