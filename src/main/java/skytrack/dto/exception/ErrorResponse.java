package skytrack.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class ErrorResponse {
    private String error;
    private String message;
    private int status;
    private LocalDateTime timestamp;
    private  Map<String, String> fieldErrors;

    public ErrorResponse(String error, String message, int status, LocalDateTime timestamp) {
        this.error = error;
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
        this.fieldErrors = null;
    }

    public ErrorResponse(String error, String message, int status, LocalDateTime timestamp, Map<String, String> fieldErrors) {
        this.error = error;
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
        this.fieldErrors = fieldErrors;
    }
}
