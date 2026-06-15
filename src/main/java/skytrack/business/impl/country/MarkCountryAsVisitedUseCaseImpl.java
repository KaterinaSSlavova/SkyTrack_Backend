package skytrack.business.impl.country;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.exception.country.VisitedCountryNotFoundException;
import skytrack.business.service.UserService;
import skytrack.business.useCase.country.MarkCountryAsVisitedUseCase;
import skytrack.persistence.entity.UserEntity;
import skytrack.persistence.entity.VisitedCountryEntity;
import skytrack.persistence.repo.VisitedCountryRepository;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MarkCountryAsVisitedUseCaseImpl implements MarkCountryAsVisitedUseCase {
    private final UserService userService;
    private final VisitedCountryRepository visitedCountryRepository;

    @Override
    public void markCountry(String countryCode) {
        UserEntity user = userService.getLoggedUser();
        if(visitedCountryRepository.existsByCountryCodeAndUserId(countryCode, user.getId())) {
            return;
        }

        String countryName = new Locale("",  countryCode).getDisplayCountry(Locale.ENGLISH);

        VisitedCountryEntity country = VisitedCountryEntity.builder()
                .countryCode(countryCode)
                .countryName(countryName)
                .user(user)
                .build();
        visitedCountryRepository.save(country);
    }
}
