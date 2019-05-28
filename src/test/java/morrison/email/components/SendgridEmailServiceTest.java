package morrison.email.components;

import morrison.email.domains.Email;
import morrison.email.exceptions.EmailServiceException;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author Daniel Morrison
 * @since 28/05/2019.
 */
public class SendgridEmailServiceTest {

    @Test
    public void shouldReturnResult() {
        SendgridEmailService service = new SendgridEmailService(mockWebClient(Mono.just(Collections.singletonMap("Ok", "yep")), Map.class), "key");
        Email email = new Email()
                .setTo(Collections.singletonList("to"))
                .setCc(Collections.singletonList("cc"))
                .setBcc(Collections.singletonList("bcc"))
                .setFrom("from")
                .setSubject("subject")
                .setBody("body");
        StepVerifier.create(service.sendEmail(email))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    public void shouldReturnFailure() {
        SendgridEmailService service = new SendgridEmailService(mockWebClient(Mono.error(new WebClientResponseException(400, "boom", null, null, null)), Map.class), "key");
        Email email = new Email()
                .setTo(Collections.singletonList("to"))
                .setCc(Collections.singletonList("cc"))
                .setBcc(Collections.singletonList("bcc"))
                .setFrom("from")
                .setSubject("subject")
                .setBody("body");
        StepVerifier.create(service.sendEmail(email))
                .expectError(EmailServiceException.class);
    }

    @SuppressWarnings("unchecked")
    private <T> WebClient mockWebClient(Mono<T> value, Class<T> type) {
        WebClient mockWebClient = Mockito.mock(WebClient.class);
        WebClient.RequestBodySpec bodySpec = Mockito.mock(WebClient.RequestBodySpec.class);
        WebClient.RequestBodyUriSpec uriSpec = Mockito.mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestHeadersSpec headersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);

        when(mockWebClient.post()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(bodySpec);
        when(bodySpec.header(anyString(), anyString())).thenReturn(bodySpec);
        when(bodySpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(bodySpec);
        when(bodySpec.body(any(BodyInserter.class))).thenReturn(headersSpec);
        when(headersSpec.accept(MediaType.APPLICATION_JSON)).thenReturn(bodySpec);
        when(bodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(type)).thenReturn(value);

        return mockWebClient;
    }

}
