package example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@RestController
@SpringBootApplication
public class message_service {

    private final WebClient database;

    public message_service() {
        database = WebClient.builder()
                .baseUrl("http://localhost:8085")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8085"))
                .build();
    }

    // Get all messages, Get messages from specific Producer
    @GetMapping("/messages")
    List<Message> getMessages(@RequestParam(required = false) String producerId) {
        List<Message> msgs = database.get()
                .uri(uriBuilder -> {
                    var uri = uriBuilder.path("/messages");

                    if (producerId != null && !producerId.equals("")) {
                        uri.queryParam("producerId", producerId);
                    }

                    return uri.build();
                })
                .retrieve()
                .bodyToFlux(Message.class)
                .collectList().block();

        return msgs;
    }

    // Create Message
    @PostMapping("/messages")
    void createMessage(@RequestBody Message m) {
        database.post()
                .uri("/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(m), Message.class)
                .retrieve()
                .bodyToMono(Void.class).block();

    }

    // Delete message
    @DeleteMapping("/messages/{id}")
    void deleteMessage(@PathVariable String id) {
        database.delete()
                .uri("/messages/" + id)
                .retrieve()
                .bodyToMono(Void.class).block();
    }

    // Get My Messages
    @GetMapping("/myMessages")
    List<Message> getMyMessages(@RequestHeader("USER-ID") String userId) {
        List<Subscription> subs = database.get()
                .uri("/subscriptions/subscriber/" + userId)
                .retrieve()
                .bodyToFlux(Subscription.class)
                .collectList().block();

        List<Message> myMessages = new ArrayList<>();

        for (Subscription s : subs) {
            var l = getMessages(s.getProducerId());
            myMessages.addAll(l);
        }

        return myMessages;
    }

    public static void main(String[] args) {
        var app = new SpringApplication(message_service.class);
        app.run(args);
    }
}