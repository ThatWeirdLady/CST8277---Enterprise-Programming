package example;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class MyApplication {

    private final MemberRepository memberRepository;
    private final MessageRepository messageRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public MyApplication(MemberRepository memberRepository, MessageRepository messageRepository,
            SubscriptionRepository subscriptionRepository) {
        this.memberRepository = memberRepository;
        this.messageRepository = messageRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @GetMapping("/members")
    List<Member> getMembers() {
        return memberRepository.findAll();
    }

    @GetMapping("/members/{id}")
    Optional<Member> getMemberById(@PathVariable String id) {
        return memberRepository.findById(id);
    }

    @DeleteMapping("/members/{id}")
    void deleteMemberById(@PathVariable String id) {
        memberRepository.deleteById(id);
    }

    @PostMapping("/members")
    Member createMember(@RequestBody Member member) {
        return member;
    }

    @PutMapping("/members/{id}")
    Member updateMember(@PathVariable String id, @RequestBody Member member) {
        return memberRepository.save(member);
    }

    @GetMapping("/messages")
    List<Message> getMessages() {
        return messageRepository.findAll();
    }

    @GetMapping("/subscriptions")
    List<Subscription> getSubscriptions() {
        return subscriptionRepository.findAll();
    }

    @GetMapping("/subscriptions/producer/{producerId}")
    List<Subscription> getSubscriptionsByProducerId(@PathVariable String producerId) {
        return subscriptionRepository.findByProducerId(producerId);
    }

    @GetMapping("/subscriptions/subscriber/{subscriberId}")
    List<Subscription> getSubscriptionsBySubscriberId(@PathVariable String subscriberId) {
        return subscriptionRepository.findBySubscriberId(subscriberId);
    }

    @GetMapping("/subscriptions/{id}")
    Optional<Subscription> getSubscriptionById(@PathVariable String id) {
        return subscriptionRepository.findById(id);
    }

    public static void main(String[] args) {
        var app = new SpringApplication(MyApplication.class);
        app.run(args);
    }
}