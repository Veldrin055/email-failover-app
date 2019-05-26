package morrison.email.handlers;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author Daniel Morrison
 * @since 26/05/2019.
 */
public class SendEmailHandler {
    public Mono<ServerResponse> send(ServerRequest serverRequest) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just("OK"), String.class);
    }
}
