package org.ac.cst8277.hailey.jennifer;

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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.web.bind.annotation.RequestHeader;
import reactor.core.publisher.Mono;

@RestController
@SpringBootApplication
public class MemberManagementService {

    private final WebClient database;

    public MemberManagementService() {
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

    @GetMapping("/token")
    public ResponseEntity<Object> token(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        LoginSession sess = new LoginSession(principal.getAttribute("login"));
        LoginSession out = database.post()
                .uri("/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(sess), LoginSession.class)
                .retrieve()
                .bodyToMono(LoginSession.class).block();
        System.out.println(out.id);
        System.out.println(out.userId);
        System.out.println(out.token);
        System.out.println(out.validUntil);

        var x = Collections.singletonMap("token", out.getToken());
        return new ResponseEntity<>(x, HttpStatus.OK);
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
        var app = new SpringApplication(MemberManagementService.class);
        app.run(args);
    }
}