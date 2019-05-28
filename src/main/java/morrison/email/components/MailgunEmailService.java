package morrison.email.components;

import morrison.email.domains.Email;
import morrison.email.exceptions.EmailServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * Webclient implementation for Mailgun (mailgun.com)
 * @author Daniel Morrison
 * @since 27/05/2019.
 */
public class MailgunEmailService implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(MailgunEmailService.class);

    private final WebClient webClient;
    private final String domain;
    private final String apiKey;

    public MailgunEmailService(WebClient webClient, String domain, String apiKey) {
        this.webClient = webClient;
        this.domain = domain;
        this.apiKey = apiKey;
    }

    @Override
    public Mono<Boolean> sendEmail(Email email) {
        return Mono.just(email)
                .map(e -> {
                    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
                    e.getTo().forEach(to -> map.set("to", to));
                    e.getCc().forEach(cc -> map.set("cc", cc));
                    e.getBcc().forEach(bcc -> map.set("bcc", bcc));
                    map.set("subject", e.getSubject());
                    map.set("text", e.getBody());
                    map.set("from", e.getFrom());
                    return map;
                })
                .flatMap(msg -> webClient
                        .post()
                        .uri(domain + "/messages")
                        .header("Authorization", "Basic " + Base64Utils
                                        .encodeToString(("api:" + apiKey).getBytes(Charset.forName("UTF-8")))
                        )
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .body(BodyInserters.fromMultipartData(msg))
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(Map.class)
                        .map(m -> true)
                        .onErrorMap(err -> {
                            logger.error("Mailgun call failed", err);
                            return new EmailServiceException(err.getMessage());
                        }));
    }
}
