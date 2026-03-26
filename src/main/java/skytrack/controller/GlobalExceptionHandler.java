package skytrack.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import skytrack.business.exception.FlightNotFoundException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private ProblemDetail createProblemDetail(HttpStatus status, String title, String detail){
        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setTitle(title);
        problemDetail.setDetail(detail);
        return problemDetail;
    }
//
//    @ExceptionHandler(FlightNotFoundException.class)
//    public ResponseEntity<ProblemDetail> handleFlightNotFoundException(FlightNotFoundException ex){
//        log.error("FlightNotFoundException occurred.", ex);
//        return new ResponseEntity<>(
//
//        );
//    }

}
