package morrison.email.components;

import morrison.email.domains.Email;
import morrison.email.domains.sendgrid.Content;
import morrison.email.domains.sendgrid.EmailAddress;
import morrison.email.domains.sendgrid.Personalization;
import morrison.email.domains.sendgrid.Send;
import morrison.email.exceptions.EmailServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.inject.Named;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Daniel Morrison
 * @since 27/05/2019.
 */
public class SendgridEmailService implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(SendgridEmailService.class);

    private final WebClient webClient;

    public SendgridEmailService(@Named("sendgrid") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<Boolean> sendEmail(Email email) {
        return Mono.just(email)
                .map(e -> new Send()
                                .setFrom(EmailAddress.from(e.getFrom()))
                                .setReplyTo(EmailAddress.from(e.getFrom()))
                                .setPersonalizations(Collections.singletonList(new Personalization()
                                                .setTo(stringsToEmails(e.getTo()))
                                                .setCc(stringsToEmails(e.getCc()))
                                                .setBcc(stringsToEmails(e.getBcc()))
                                                .setSubject(e.getSubject())

                                ))
                                .setContent(Collections.singletonList(new Content()
                                                .setType("text/plainn")
                                                .setValue(e.getBody()))
                                )
                )
                .flatMap(msg -> webClient
                        .post()
                        .uri("/v3/mail/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromObject(msg))
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(Map.class)
                        .map(m -> true)
                        .onErrorMap(err -> {
                            logger.error("Mailgun call failed", err);
                            return new EmailServiceException(err.getMessage());
                        }));
    }

    private List<EmailAddress> stringsToEmails(List<String> strings) {
        return strings.stream().map(EmailAddress::from).collect(Collectors.toList());
    }
}
