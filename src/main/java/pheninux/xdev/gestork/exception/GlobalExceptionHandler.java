package pheninux.xdev.gestork.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomServiceException.class)
    public ResponseEntity<String> handleCustomServiceException(CustomServiceException ex) {
        HttpStatus status;

        switch (ex.getErrorCode()) {
            case 404:
                status = HttpStatus.NOT_FOUND;
                break;
            case 400:
                status = HttpStatus.BAD_REQUEST;
                break;
            case 500:
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                break;
            default:
                status = HttpStatus.INTERNAL_SERVER_ERROR; // Par défaut
        }
        return new ResponseEntity<>(ex.getMessage(), status);
    }
}
