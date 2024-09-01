package example;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@RestController
@SpringBootApplication
public class memberManagement_service {

    private final WebClient database;

    public memberManagement_service() {
        database = WebClient.builder()
                .baseUrl("http://172.20.0.6:8085")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", "http://172.20.0.6:8085"))
                .build();
    }

    // Get all Members
    // get Member by username
    @GetMapping("/members")
    List<Member> getMembers(@RequestParam(required = false) String username) {
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
        return members;
    }

    // Get user by id
    @GetMapping("/members/{id}")
    List<Member> getMember(@PathVariable String id) {
        List<Member> member = database.get()
                .uri("/members/" + id)
                .retrieve()
                .bodyToFlux(Member.class)
                .collectList()
                .block();
        return member;
    }

    // Create Member
    @PostMapping("/members")
    Member createMember(@RequestBody Member m) {
        database.post()
                .uri("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(m), Member.class)
                .retrieve()
                .bodyToMono(Member.class).block();
        return m;
    }

    // Update Member
    @PutMapping("/members/{id}")
    Member updateMember(@PathVariable String id, @RequestBody Member m) {
        database.put()
                .uri("/members/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(m), Member.class)
                .retrieve()
                .bodyToMono(Member.class).block();
        return m;

    }

    // Delete Member
    @DeleteMapping("/members/{id}")
    void deleteMember(@PathVariable String id) {
        database.delete()
                .uri("members/" + id)
                .retrieve()
                .bodyToMono(Void.class).block();

    }

    public static void main(String[] args) {
        var app = new SpringApplication(memberManagement_service.class);
        app.run(args);
    }
}