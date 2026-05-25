package skytrack.business.useCase.service;

import java.time.LocalDate;

public interface PassengerValidation {
    void validateAge(LocalDate birthDate);
    void validatePassportNumber(String passportNumber);
}
