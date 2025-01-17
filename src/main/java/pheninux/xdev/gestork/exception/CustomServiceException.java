package pheninux.xdev.gestork.exception;

import org.springframework.dao.DataAccessException;

import java.io.Serial;

public class CustomServiceException extends Throwable {

    @Serial
    private static final long serialVersionUID = 1L;

    public CustomServiceException(String message) {
        super(message);
    }

    public CustomServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
