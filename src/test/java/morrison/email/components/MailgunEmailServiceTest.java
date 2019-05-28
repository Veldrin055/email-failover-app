package morrison.email.components;

import morrison.email.domains.Email;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.WebClient;
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
public class MailgunEmailServiceTest {

    @Test
    public void shouldGetResponse() {
        MailgunEmailService service = new MailgunEmailService(mockWebClient(Mono.just(Collections.singletonMap("ok", "yep")), Map.class), "domain", "key");

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
        when(bodySpec.contentType(MediaType.MULTIPART_FORM_DATA)).thenReturn(bodySpec);
        when(bodySpec.body(any(BodyInserter.class))).thenReturn(headersSpec);
        when(headersSpec.accept(MediaType.APPLICATION_JSON)).thenReturn(bodySpec);
        when(bodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(type)).thenReturn(value);

        return mockWebClient;
    }

}
