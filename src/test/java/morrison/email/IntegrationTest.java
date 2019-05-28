package morrison.email;

import morrison.email.domains.Email;
import morrison.email.domains.ResponseMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author Daniel Morrison
 * @since 28/05/2019.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {App.class})
public class IntegrationTest {

    @LocalServerPort
    private int port;

    @SpyBean(name = "mailgun")
    private WebClient mailgun;

    @SpyBean(name = "sendgrid")
    private WebClient sendgrid;

    @Inject
    private TestRestTemplate restTemplate;

    @Test
    public void shouldGetResponse() {
        Email email = new Email()
                .setTo(Collections.singletonList("to"))
                .setCc(Collections.singletonList("cc"))
                .setBcc(Collections.singletonList("bcc"))
                .setFrom("from")
                .setSubject("subject")
                .setBody("body");

        mailgun(Mono.just(Collections.singletonMap("result", "ok")), Map.class);

        final ResponseEntity<ResponseMessage> response =
                restTemplate.postForEntity("http://localhost:" + port + "/send", email, ResponseMessage.class);

        assertThat(response, is(notNullValue()));
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.OK)));
        final ResponseMessage body = response.getBody();
        assertThat(body, is(notNullValue()));
        assertThat(body.getMessage(), is(equalTo("Message sent")));
    }

    @SuppressWarnings("unchecked")
    private <T> void mailgun(Mono<T> value, Class<T> type) {
        WebClient.RequestBodySpec bodySpec = Mockito.mock(WebClient.RequestBodySpec.class);
        WebClient.RequestBodyUriSpec uriSpec = Mockito.mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestHeadersSpec headersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);

        when(mailgun.post()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(bodySpec);
        when(bodySpec.header(anyString(), anyString())).thenReturn(bodySpec);
        when(bodySpec.contentType(MediaType.MULTIPART_FORM_DATA)).thenReturn(bodySpec);
        when(bodySpec.body(any(BodyInserter.class))).thenReturn(headersSpec);
        when(headersSpec.accept(MediaType.APPLICATION_JSON)).thenReturn(bodySpec);
        when(bodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(type)).thenReturn(value);
    }

    @SuppressWarnings("unchecked")
    private <T> void sendgrid(Mono<T> value, Class<T> type) {
        WebClient.RequestBodySpec bodySpec = Mockito.mock(WebClient.RequestBodySpec.class);
        WebClient.RequestBodyUriSpec uriSpec = Mockito.mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestHeadersSpec headersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);

        when(sendgrid.post()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(bodySpec);
        when(bodySpec.header(anyString(), anyString())).thenReturn(bodySpec);
        when(bodySpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(bodySpec);
        when(bodySpec.body(any(BodyInserter.class))).thenReturn(headersSpec);
        when(headersSpec.accept(MediaType.APPLICATION_JSON)).thenReturn(bodySpec);
        when(bodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(type)).thenReturn(value);
    }
}
