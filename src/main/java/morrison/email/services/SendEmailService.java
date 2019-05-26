package morrison.email.services;

import morrison.email.components.EmailService;
import morrison.email.domains.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author Daniel Morrison
 * @since 26/05/2019.
 */
@Service
public class SendEmailService {

    private final EmailService primary;
    private final EmailService secondary;

    @Autowired
    public SendEmailService(EmailService primary, EmailService secondary) {
        this.primary = primary;
        this.secondary = secondary;
    }

    public Mono<Boolean> sendEmail(Email email) {
        Mono.just(email)
                .map(primary::sendEmail)
                .onErrorResume(EmailServiceException.class, e -> secondary.sendEmail(email))
    }
}
