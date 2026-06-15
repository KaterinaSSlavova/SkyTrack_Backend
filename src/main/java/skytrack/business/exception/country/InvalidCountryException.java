package skytrack.business.exception.country;

public class InvalidCountryException extends RuntimeException {
    public InvalidCountryException(String countryName) {
        super( "Country '" + countryName + "' is not a valid ISO country.");
    }
}
