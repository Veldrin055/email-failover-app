package morrison.email.domains;

import morrison.email.exceptions.EmailAppException;
import reactor.core.publisher.Mono;

/**
 * @author Daniel Morrison
 * @since 26/05/2019.
 */
public class ErrorMessage {

    private int status;
    private String message;

    private ErrorMessage(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public static Mono<ErrorMessage> fromError(EmailAppException e) {
        return Mono.just(new ErrorMessage(e.getStatus().value(), e.getMessage()));
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
