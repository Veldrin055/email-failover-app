package morrison.email.handlers;

import morrison.email.domains.Email;
import morrison.email.domains.ErrorMessage;
import morrison.email.exceptions.EmailAppException;
import morrison.email.services.SendEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author Daniel Morrison
 * @since 26/05/2019.
 */
@Component
public class SendEmailHandler {

    private final SendEmailService sendEmailService;

    @Autowired
    public SendEmailHandler(SendEmailService sendEmailService) {
        this.sendEmailService = sendEmailService;
    }


    public Mono<ServerResponse> send(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Email.class)
                .map(sendEmailService::sendEmail)
                .flatMap(booleanMono -> ServerResponse
                                .ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(booleanMono, Boolean.class)
                )
                .onErrorMap(EmailAppException.class, e -> ServerResponse
                                .status(e.getStatus())
                                .body(ErrorMessage.fromError(e), ErrorMessage.class)
                );
    }
}
