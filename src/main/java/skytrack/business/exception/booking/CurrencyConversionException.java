package skytrack.business.exception.booking;

public class CurrencyConversionException extends RuntimeException {
    public CurrencyConversionException(String externalId) {
        super("Failed to convert currency for flight: "+ externalId);
    }
}
