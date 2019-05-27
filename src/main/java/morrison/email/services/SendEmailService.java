package morrison.email.services;

import morrison.email.components.EmailService;
import morrison.email.domains.Email;
import morrison.email.exceptions.EmailAppException;
import morrison.email.exceptions.EmailServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Orchestration service layer for handling the failover logic. Will first attempt to resolve using the primary service,
 * then failover to the second. If that fails as well, a default false will be returned.
 * @author Daniel Morrison
 * @since 26/05/2019.
 */
@Service
public class SendEmailService {

    private final EmailService primary;
    private final EmailService secondary;

    @Inject
    public SendEmailService(@Named("primary") EmailService primary, @Named("secondary") EmailService secondary) {
        this.primary = primary;
        this.secondary = secondary;
    }

    public Mono<Boolean> sendEmail(Email email) {
        return Mono.just(email)
                .flatMap(e -> primary.sendEmail(email))
                .onErrorResume(EmailServiceException.class, e -> Mono.just(email).flatMap(secondary::sendEmail))
                .onErrorMap(t -> new EmailAppException(HttpStatus.SERVICE_UNAVAILABLE, "Both attempts failed"));
    }
}
