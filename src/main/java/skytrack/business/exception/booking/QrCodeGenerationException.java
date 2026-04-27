package skytrack.business.exception.booking;

public class QrCodeGenerationException extends RuntimeException {
    public QrCodeGenerationException(Throwable cause) {
        super("QR Code generation failed!", cause);
    }
}
