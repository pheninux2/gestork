package pheninux.xdev.gestork.exception;

import java.io.Serial;

public class CustomServiceException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;
    private final int errorCode;

    public CustomServiceException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public CustomServiceException(String message, Throwable cause, int errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
