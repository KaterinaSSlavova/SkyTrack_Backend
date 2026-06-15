package skytrack.business.exception.country;

public class VisitedCountryNotFoundException extends RuntimeException {
    public VisitedCountryNotFoundException(String countryCode) {
        super("Visited country with code '" + countryCode + "' was not found.");
    }
}
