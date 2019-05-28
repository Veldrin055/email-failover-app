package morrison.email.handlers;

import morrison.email.domains.Email;
import morrison.email.domains.ResponseMessage;
import morrison.email.exceptions.EmailAppException;
import morrison.email.services.SendEmailService;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.inject.Inject;

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
        validator = new LocalValidatorFactoryBean();
    }

    public Mono<ServerResponse> send(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Email.class)
                .flatMap(email -> {
                    Errors errors = new BeanPropertyBindingResult(email, email.getClass().getName());
                    validator.validate(email, errors);
                    if (errors.getAllErrors().isEmpty()) {
                        return Mono.just(email);
                    } else {
                        return Mono.error(new EmailAppException(HttpStatus.NOT_ACCEPTABLE,
                                errors.getAllErrors().stream()
                                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                        .reduce("", (s, s2) -> s + '\n' + s2)));
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
