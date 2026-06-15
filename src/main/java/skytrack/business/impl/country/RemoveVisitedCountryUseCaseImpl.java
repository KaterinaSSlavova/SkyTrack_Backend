package skytrack.business.impl.country;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import skytrack.business.exception.country.VisitedCountryNotFoundException;
import skytrack.business.service.UserService;
import skytrack.business.useCase.country.RemoveVisitedCountryUseCase;
import skytrack.persistence.entity.UserEntity;
import skytrack.persistence.entity.VisitedCountryEntity;
import skytrack.persistence.repo.VisitedCountryRepository;

@Service
@RequiredArgsConstructor
public class RemoveVisitedCountryUseCaseImpl implements RemoveVisitedCountryUseCase {
    private final UserService userService;
    private final VisitedCountryRepository visitedCountryRepository;

    @Override
    public void removeCountry(String countryCode) {
        UserEntity user = userService.getLoggedUser();
        VisitedCountryEntity country = visitedCountryRepository
                .findByCountryCodeAndUserId(countryCode, user.getId())
                .orElseThrow(() -> new VisitedCountryNotFoundException(countryCode));
        visitedCountryRepository.delete(country);
    }
}
