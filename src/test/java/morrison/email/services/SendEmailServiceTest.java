package morrison.email.services;

import morrison.email.components.EmailService;
import morrison.email.domains.Email;
import morrison.email.exceptions.EmailServiceException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * @author Daniel Morrison
 * @since 27/05/2019.
 */
@RunWith(MockitoJUnitRunner.class)
public class SendEmailServiceTest {

    @Mock
    private EmailService primary;

    @Mock
    private EmailService secondary;

    private SendEmailService service;

    @Before
    public void setUp() throws Exception {
        service = new SendEmailService(primary, secondary);
    }

    @Test
    public void shouldUsePrimary() {
        Email email = new Email();
        given(primary.sendEmail(email)).willReturn(Mono.just(true));
        StepVerifier.create(service.sendEmail(email))
                .assertNext(bool -> assertThat(bool, is(equalTo(true))))
                .verifyComplete();
        verify(primary).sendEmail(email);
        verifyZeroInteractions(secondary);
    }

    @Test
    public void shouldFallbackToSecondaryOnPrimaryFailure() {
        Email email = new Email();
        given(primary.sendEmail(email)).willReturn(Mono.error(new EmailServiceException("boom")));
        given(secondary.sendEmail(email)).willReturn(Mono.just(true));
        StepVerifier.create(service.sendEmail(email))
                .assertNext(bool -> assertThat(bool, is(equalTo(true))))
                .verifyComplete();
        verify(primary).sendEmail(email);
        verify(secondary).sendEmail(email);
    }

    @Test
    public void shouldErrorWhenBothServicesFail() {
        Email email = new Email();
        given(primary.sendEmail(email)).willReturn(Mono.error(new EmailServiceException("boom")));
        given(secondary.sendEmail(email)).willReturn(Mono.error(new EmailServiceException("boom again")));
        StepVerifier.create(service.sendEmail(email))
                .assertNext(bool -> assertThat(bool, is(equalTo(false))))
                .verifyComplete();
        verify(primary).sendEmail(email);
        verify(secondary).sendEmail(email);
    }
}
