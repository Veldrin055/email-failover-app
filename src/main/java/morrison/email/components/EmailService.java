package morrison.email.components;

import morrison.email.domains.Email;
import reactor.core.publisher.Mono;

/**
 * @author Daniel Morrison
 * @since 26/05/2019.
 */
public interface EmailService {

    /**
     * Calls the email service provider implementation and sends an email
     *
     * @param email Message to be sent
     * @return true if the call worked
     */
    Mono<Boolean> sendEmail(Email email);
}
