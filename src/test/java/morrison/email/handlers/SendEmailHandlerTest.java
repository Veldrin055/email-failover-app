package morrison.email.handlers;

import morrison.email.domains.Email;
import morrison.email.exceptions.EmailAppException;
import morrison.email.services.SendEmailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * @author Daniel Morrison
 * @since 27/05/2019.
 */
@RunWith(MockitoJUnitRunner.class)
public class SendEmailHandlerTest {

    @Mock
    private SendEmailService emailService;

    @Mock
    private ServerRequest serverRequest;


    @InjectMocks
    private SendEmailHandler handler;

    @Test
    public void shouldHandleSuccess() {
        Email email = new Email();
        given(serverRequest.bodyToMono(Email.class)).willReturn(Mono.just(email));
        given(emailService.sendEmail(email)).willReturn(Mono.just(true));

        StepVerifier.create(handler.send(serverRequest))
                .assertNext(response -> assertThat(response.statusCode(), is(equalTo(HttpStatus.OK))))
                .verifyComplete();
        verify(emailService).sendEmail(email);
    }

    @Test
    public void shouldHandleError() {
        Email email = new Email();
        given(serverRequest.bodyToMono(Email.class)).willReturn(Mono.just(email));
        given(emailService.sendEmail(email)).willReturn(Mono.error(new EmailAppException(HttpStatus.BAD_REQUEST, "boom")));

        StepVerifier.create(handler.send(serverRequest))
                .assertNext(response -> assertThat(response.statusCode(), is(equalTo(HttpStatus.BAD_REQUEST))))
                .verifyComplete();
        verify(emailService).sendEmail(email);
    }

}
