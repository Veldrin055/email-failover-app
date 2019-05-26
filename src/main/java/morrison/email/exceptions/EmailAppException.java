package morrison.email.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author Daniel Morrison
 * @since 26/05/2019.
 */
public class EmailAppException extends Throwable {


    private final HttpStatus status;

    public EmailAppException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

}
