package example;

import java.util.Collections;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
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

    // Get all subscriptions
    @GetMapping("/subscriptions")
    List<Subscription> getSubscriptions() {
        List<Subscription> out = database.get()
                .uri("/subscriptions")
                .retrieve()
                .bodyToFlux(Subscription.class)
                .collectList().block();
        return out;
    }

    // Create Subscription
    @PostMapping("/producers/{producerId}/subscribe")
    void subscribe(@RequestHeader("SUBSCRIBER-ID") String subscriberId, @PathVariable String producerId) {
        Subscription newSub = new Subscription(producerId, subscriberId);

        database.post()
                .uri("/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(newSub), Subscription.class)
                .retrieve()
                .bodyToMono(Void.class).block();
        return;
    }

    @PostMapping("/producers/{producerId}/unsubscribe")
    void unsubscribe(@RequestHeader("SUBSCRIBER-ID") String subscriberId, @PathVariable String producerId) {
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
    }

    public static void main(String[] args) {
        var app = new SpringApplication(SubscribeService.class);
        app.run(args);
    }
}