package morrison.email;

import morrison.email.domains.Email;
import morrison.email.domains.ResponseMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;

import javax.inject.Inject;

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
        Email email = new Email();
        restTemplate.postForEntity("http://localhost:" + port + "/send", email, ResponseMessage.class);
    }
}
