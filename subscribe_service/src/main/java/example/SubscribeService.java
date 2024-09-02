package example;

import java.util.Collections;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@RestController
@SpringBootApplication
public class SubscribeService {

    private final WebClient database;

    public SubscribeService() {
        database = WebClient.builder()
                .baseUrl("http://localhost:8085")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8085"))
                .build();
    }

    public Boolean validate(String token) {
        Boolean valid = database.get()
                .uri("/auth/validate")
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(Boolean.class).block();
        return valid;
    }

    // // Get all subscriptions
    // @GetMapping("/subscriptions")
    // List<Subscription> getSubscriptions() {
    // List<Subscription> out = database.get()
    // .uri("/subscriptions")
    // .retrieve()
    // .bodyToFlux(Subscription.class)
    // .collectList().block();
    // return out;
    // }

    // Create Subscription
    @PostMapping("/producers/{producerId}/subscribe")
    ResponseEntity<Object> subscribe(@RequestHeader("Authorization") String token,
            @RequestHeader("USER-ID") String subscriberId, @PathVariable String producerId) {
        Subscription newSub = new Subscription(producerId, subscriberId);
        boolean valid = validate(token);
        if (!valid)
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        database.post()
                .uri("/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(newSub), Subscription.class)
                .retrieve()
                .bodyToMono(Void.class).block();
        return new ResponseEntity<>("Subscription Added", HttpStatus.OK);
    }

    // 'Delete' subscription
    @PostMapping("/producers/{producerId}/unsubscribe")
    ResponseEntity<Object> unsubscribe(@RequestHeader("Authorization") String token,
            @RequestHeader("USER-ID") String subscriberId, @PathVariable String producerId) {

        boolean valid = validate(token);
        if (!valid)
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        List<Subscription> subscriptions = database.get()
                .uri("/subscriptions/producer/" + producerId)
                .retrieve()
                .bodyToFlux(Subscription.class)
                .collectList().block();

        for (Subscription s : subscriptions) {
            if (s.getSubscriberId().equals(subscriberId)) {
                database.delete().uri("/subscriptions/" + s.getId()).retrieve()
                        .bodyToMono(Void.class).block();
                break;
            }
        }
        return new ResponseEntity<>("Subscription Removed", HttpStatus.OK);
    }

    public static void main(String[] args) {
        var app = new SpringApplication(SubscribeService.class);
        app.run(args);
    }
}