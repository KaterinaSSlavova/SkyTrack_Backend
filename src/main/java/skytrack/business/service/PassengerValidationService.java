package skytrack.business.service;

import org.springframework.stereotype.Service;
import skytrack.business.exception.user.InvalidPassportException;
import skytrack.business.exception.user.UserTooOldException;
import skytrack.business.exception.user.UserTooYoungException;
import skytrack.business.useCase.service.PassengerValidation;

import java.time.LocalDate;
import java.time.Period;

@Service
public class PassengerValidationService implements PassengerValidation {

    @Override
    public void validateAge(LocalDate birthDate) {
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        if(age < 16) throw new UserTooYoungException();
        if(age > 120) throw new UserTooOldException();
    }

    @Override
    public void validatePassportNumber(String passportNumber) {
        if (passportNumber == null || passportNumber.isBlank()) {
            throw new InvalidPassportException("Passport number is required");
        }

        passportNumber = passportNumber.trim();
        if (passportNumber.length() < 6 || passportNumber.length() > 12) {
            throw new InvalidPassportException("Passport must contain between 6 and 12 characters");
        }

        if (!passportNumber.matches("^[A-Za-z0-9]+$")) {
            throw new InvalidPassportException("Passport must contain only letters and numbers");
        }
    }
}
