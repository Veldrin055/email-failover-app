package morrison.email.handlers;

import morrison.email.domains.Email;
import morrison.email.domains.ResponseMessage;
import morrison.email.exceptions.EmailAppException;
import morrison.email.services.SendEmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * Handle POST calls from the client
 *
 * @author Daniel Morrison
 * @since 26/05/2019.
 */
@Component
public class SendEmailHandler {

    private final SendEmailService sendEmailService;
    private final Validator validator;

    @Inject
    public SendEmailHandler(SendEmailService sendEmailService) {
        this.sendEmailService = sendEmailService;
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public Mono<ServerResponse> send(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Email.class)
                .flatMap(email -> {
                    final Set<ConstraintViolation<Email>> constraintViolations = validator.validate(email);
                    if (constraintViolations.isEmpty()) {
                        return Mono.just(email);
                    } else {
                        return Mono.error(new EmailAppException(HttpStatus.NOT_ACCEPTABLE,
                                constraintViolations.stream()
                                        .map(ConstraintViolation::getMessage)
                                        .reduce("", (s, s2) -> s + (s.isEmpty() ? "" : ", ") + s2)));
                    }
                })
                .flatMap(sendEmailService::sendEmail)
                .flatMap(booleanMono -> ServerResponse
                                .ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(ResponseMessage.ok("Message sent"), ResponseMessage.class)
                )
                .onErrorResume(EmailAppException.class, e -> ServerResponse
                                .status(e.getStatus())
                                .body(ResponseMessage.fromError(e), ResponseMessage.class)
                );
    }
}
