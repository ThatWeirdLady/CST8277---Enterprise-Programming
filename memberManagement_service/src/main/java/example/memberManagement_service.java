package example;

import java.util.Collections;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.bind.annotation.RequestHeader;
import reactor.core.publisher.Mono;

@RestController
@SpringBootApplication
public class memberManagement_service {

    private final WebClient database;

    public memberManagement_service() {
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

    // Get all Members
    // get Member by username
    @GetMapping("/members")
    ResponseEntity<Object> getMembers(@RequestHeader("Authorization") String token,
            @RequestParam(required = false) String username) {
        boolean valid = validate(token);
        if (!valid)
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        List<Member> members = database.get()
                .uri(uriBuilder -> {
                    var uri = uriBuilder.path("/members");

                    if (username != null && !username.equals("")) {
                        uri.queryParam("username", username);
                    }

                    return uri.build();
                })
                .retrieve()
                .bodyToFlux(Member.class)
                .collectList().block();
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    // Get user by id
    @GetMapping("/members/{id}")
    ResponseEntity<Object> getMember(@RequestHeader("Authorization") String token, @PathVariable String id) {
        boolean valid = validate(token);
        if (!valid)
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        Member member = database.get()
                .uri("/members/" + id)
                .retrieve()
                .bodyToMono(Member.class).block();
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    // Create Member
    @PostMapping("/members")
    Member createMember(@RequestBody Member m) {
        Member out = database.post()
                .uri("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(m), Member.class)
                .retrieve()
                .bodyToMono(Member.class).block();
        return out;
    }

    // Update Member
    @PutMapping("/members/{id}")
    ResponseEntity<Object> updateMember(@RequestHeader("Authorization") String token, @PathVariable String id,
            @RequestBody Member m) {
        boolean valid = validate(token);
        if (!valid)
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        database.put()
                .uri("/members/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(m), Member.class)
                .retrieve()
                .bodyToMono(Member.class).block();
        return new ResponseEntity<>(m, HttpStatus.OK);

    }

    // Delete Member
    @DeleteMapping("/members/{id}")
    ResponseEntity<Object> deleteMember(@RequestHeader("Authorization") String token, @PathVariable String id) {
        boolean valid = validate(token);
        if (!valid)
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        database.delete()
                .uri("members/" + id)
                .retrieve()
                .bodyToMono(Void.class).block();
        return new ResponseEntity<>("Deleted", HttpStatus.OK);

    }

    public static void main(String[] args) {
        var app = new SpringApplication(memberManagement_service.class);
        app.run(args);
    }
}