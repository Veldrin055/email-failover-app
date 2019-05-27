package morrison.email.config;

import morrison.email.components.EmailService;
import morrison.email.components.MailgunEmailService;
import morrison.email.components.SendgridEmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import javax.inject.Named;

/**
 * @author Daniel Morrison
 * @since 27/05/2019.
 */
@Configuration
public class AppConfig {

    @Bean(name = "primary")
    EmailService primary(@Named("mailgun") WebClient webClient, @Value("mailgun.domain") String domain) {
        return new MailgunEmailService(webClient, domain);
    }

    @Bean(name = "secondary")
    EmailService secondary(@Named("sendgrid") WebClient webClient) {
        return new SendgridEmailService(webClient);
    }

    @Bean(name = "mailgun")
    WebClient mailgunClient(@Value("mailgun.baseUrl") String baseUrl) {
        return WebClient.create(baseUrl);
    }

    @Bean(name = "sendgrid")
    WebClient sendGridClient(@Value("sendgrid.baseUrl") String baseUrl) {
        return WebClient.create(baseUrl);
    }

}
