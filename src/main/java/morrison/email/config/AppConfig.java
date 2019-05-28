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
 * Provide bindings for the email service integrations
 * @author Daniel Morrison
 * @since 27/05/2019.
 */
@Configuration
public class AppConfig {

    @Bean(name = "primary")
    EmailService primary(@Named("mailgun") WebClient webClient,
                         @Value("${mailgun.domain}") String domain,
                         @Value("${mailgun.apiKey}") String apiKey) {
        return new MailgunEmailService(webClient, domain, apiKey);
    }

    @Bean(name = "secondary")
    EmailService secondary(@Named("sendgrid") WebClient webClient,
                           @Value("${sendgrid.apiKey}") String apiKey) {
        return new SendgridEmailService(webClient, apiKey);
    }

    @Bean(name = "mailgun")
    WebClient mailgunClient(@Value("${mailgun.baseUrl}") String baseUrl) {
        return WebClient.create(baseUrl);
    }

    @Bean(name = "sendgrid")
    WebClient sendGridClient(@Value("${sendgrid.baseUrl}") String baseUrl) {
        return WebClient.create(baseUrl);
    }

}
