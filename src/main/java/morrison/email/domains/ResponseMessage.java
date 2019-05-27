package morrison.email.domains;

import morrison.email.exceptions.EmailAppException;
import reactor.core.publisher.Mono;

/**
 * Response object
 * @author Daniel Morrison
 * @since 26/05/2019.
 */
public class ResponseMessage {

    private int status;
    private String message;

    private ResponseMessage(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public static Mono<ResponseMessage> ok(String message) {
        return Mono.just(new ResponseMessage(200, message));
    }

    public static Mono<ResponseMessage> fromError(EmailAppException e) {
        return Mono.just(new ResponseMessage(e.getStatus().value(), e.getMessage()));
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
