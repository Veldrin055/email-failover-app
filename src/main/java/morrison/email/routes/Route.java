package morrison.email.routes;

import morrison.email.handlers.SendEmailHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * Controller route for exposing a REST endpoint
 * @author Daniel Morrison
 * @since 26/05/2019.
 */
@Configuration
public class Route {

    @Bean
    RouterFunction<ServerResponse> router(SendEmailHandler handler) {
        return route(RequestPredicates.POST("/send").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::send);
    }
}

