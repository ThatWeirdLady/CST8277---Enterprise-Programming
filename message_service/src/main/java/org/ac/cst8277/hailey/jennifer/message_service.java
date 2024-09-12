package org.ac.cst8277.hailey.jennifer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
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
import org.springframework.http.ResponseEntity;
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

    public Boolean validate(String token) {
        Boolean valid = database.get()
                .uri("/auth/validate")
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(Boolean.class).block();
        return valid;
    }

    List<Message> acqMessages(String producerId) {
        return database.get()
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
    }

    // Get all messages, Get messages from specific Producer
    @GetMapping("/messages")
    ResponseEntity<Object> getMessages(@RequestHeader("Authorization") String token,
            @RequestParam(required = false) String producerId) {
        if (token == null)
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        boolean valid = validate(token);
        if (!valid)
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);

        List<Message> msgs = acqMessages(producerId);

        return new ResponseEntity<>(msgs, HttpStatus.OK);
    }

    // Create Message
    @PostMapping("/messages")
    ResponseEntity<Object> createMessage(@RequestHeader("Authorization") String token, @RequestBody Message m) {
        if (token == null)
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        boolean valid = validate(token);
        if (!valid)
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);

        Message msg = database.post()
                .uri("/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(m), Message.class)
                .retrieve()
                .bodyToMono(Message.class).block();
        return new ResponseEntity<>(msg, HttpStatus.OK);

    }

    // Delete message
    @DeleteMapping("/messages/{id}")
    ResponseEntity<Object> deleteMessage(@RequestHeader("Authorization") String token, @PathVariable String id) {
        if (token == null)
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        boolean valid = validate(token);
        if (!valid)
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        database.delete()
                .uri("/messages/" + id)
                .retrieve()
                .bodyToMono(Void.class).block();
        return new ResponseEntity<>("Message Deleted", HttpStatus.OK);
    }

    // Get My Messages
    @GetMapping("/myMessages")
    ResponseEntity<Object> getMyMessages(@RequestHeader("Authorization") String token,
            @RequestHeader("USER-ID") String userId) {
        if (token == null)
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        boolean valid = validate(token);
        if (!valid)
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        List<Subscription> subs = database.get()
                .uri("/subscriptions/subscriber/" + userId)
                .retrieve()
                .bodyToFlux(Subscription.class)
                .collectList().block();

        List<Message> myMessages = new ArrayList<>();

        for (Subscription s : subs) {
            var l = acqMessages(s.getProducerId());
            myMessages.addAll(l);
        }

        return new ResponseEntity<>(myMessages, HttpStatus.OK);
    }

    public static void main(String[] args) {
        var app = new SpringApplication(message_service.class);
        app.run(args);
    }
}