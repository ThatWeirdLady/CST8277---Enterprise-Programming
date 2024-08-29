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
import org.springframework.web.bind.annotation.RequestParam;
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

    // Get all members
    @GetMapping("/members")
    List<Member> getMembers(@RequestParam(required = false) String username) {
        if (username != null && !username.equals("")) {
            return memberRepository.findByUsername(username);
        }
        return memberRepository.findAll();
    }

    // Get member by ID
    @GetMapping("/members/{id}")
    Optional<Member> getMemberById(@PathVariable String id) {
        return memberRepository.findById(id);
    }

    // Delete member
    @DeleteMapping("/members/{id}")
    void deleteMemberById(@PathVariable String id) {
        memberRepository.deleteById(id);
    }

    // Create member
    @PostMapping("/members")
    Member createMember(@RequestBody Member member) {
        return memberRepository.save(member);
    }

    // Update member
    @PutMapping("/members/{id}")
    Member updateMember(@PathVariable String id, @RequestBody Member member) {
        return memberRepository.save(member);
    }

    // Get all messages
    @GetMapping("/messages")
    List<Message> getMessages(@RequestParam(required = false) String producerId) {
        if (producerId != null && !producerId.equals("")) {
            return messageRepository.findByProducerId(producerId);
        }
        return messageRepository.findAll();
    }

    // Create Message
    @PostMapping("/messages")
    Message createMessage(@RequestBody Message message) {
        try {
            return messageRepository.save(message);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    // Update Message
    @PostMapping("/messages/{id}")
    Message updateMessage(@PathVariable String id, @RequestBody Message message) {
        return messageRepository.save(message);
    }

    // Delete Message
    @DeleteMapping("/messages/{id}")
    void deleteMessage(@PathVariable String id) {
        messageRepository.deleteById(id);
    }

    // Get all subscriptions
    @GetMapping("/subscriptions")
    List<Subscription> getSubscriptions() {
        return subscriptionRepository.findAll();
    }

    // Get all subscriptions for specific producer
    @GetMapping("/subscriptions/producer/{producerId}")
    List<Subscription> getSubscriptionsByProducerId(@PathVariable String producerId) {
        return subscriptionRepository.findByProducerId(producerId);
    }

    // Get all subscriptions for specific subscriber
    @GetMapping("/subscriptions/subscriber/{subscriberId}")
    List<Subscription> getSubscriptionsBySubscriberId(@PathVariable String subscriberId) {
        return subscriptionRepository.findBySubscriberId(subscriberId);
    }

    // Get subscription by id
    @GetMapping("/subscriptions/{id}")
    Optional<Subscription> getSubscriptionById(@PathVariable String id) {
        return subscriptionRepository.findById(id);
    }

    // Create subscription
    @PostMapping("/subscriptions")
    void createSubscription(@RequestBody Subscription subscription) {
        subscriptionRepository.save(subscription);
    }

    // Delete subscription
    @DeleteMapping("/subscriptions/{id}")
    void deleteSubscription(@PathVariable String id) {
        subscriptionRepository.deleteById(id);
    }

    public static void main(String[] args) {
        var app = new SpringApplication(MyApplication.class);
        app.run(args);
    }
}