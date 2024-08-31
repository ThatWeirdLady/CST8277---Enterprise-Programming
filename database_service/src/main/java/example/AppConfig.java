package example;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

        @Bean
        CommandLineRunner commandLineRunner(MemberRepository memberRepository, MessageRepository messageRepository,
                        SubscriptionRepository subscriptionRepository) {

                return args -> {
                        List<Member> membersList = List.of(
                                        new Member("f6be4699-ccb9-4c52-b6ae-91f5d4056fca", "JohnnyBravo", "password"),
                                        new Member("8ee28bdc-86e0-4b29-bb59-21a5306b7ec9", "BugsBunny", "password"),
                                        new Member("7dc64326-0604-4455-807e-d0ad43a18f97", "DaffyDuck", "password"),
                                        new Member("67df8454-73a8-4c99-8a69-bceec6b3e6cc", "ElmerFudd", "password"),
                                        new Member("f563bf0b-da27-4330-a030-a9248a2b1f64", "YoSamdySam", "password"));

                        memberRepository.saveAll(membersList);
                        List<Message> messagesList = List.of(
                                        new Message("2274589f-a4ef-448b-b05d-35cff66672e6", "Eeh, What's up Doc?",
                                                        "8ee28bdc-86e0-4b29-bb59-21a5306b7ec9"),
                                        new Message("e678026e-6f7b-45ad-8b8e-7ce1e62997ca",
                                                        "Sssshh… Be vewwy quiet. I'm hunting wabbit!",
                                                        "67df8454-73a8-4c99-8a69-bceec6b3e6cc"),
                                        new Message("2f01a6b8-b7f7-4e62-bd7c-756f8d36c8fb",
                                                        "Carrots are the best snack, anyone know some local farms?",
                                                        "8ee28bdc-86e0-4b29-bb59-21a5306b7ec9"),
                                        new Message("c24dc693-20a5-4eec-8597-41cf78751510",
                                                        "Hunting season just doesn't come fast enough",
                                                        "67df8454-73a8-4c99-8a69-bceec6b3e6cc"),
                                        new Message("3f892819-5a32-4790-97f3-188d9bdf8077", "Yosemite Sam is a phoney!",
                                                        "67df8454-73a8-4c99-8a69-bceec6b3e6cc"),
                                        new Message("6e9f65dd-9694-4346-ab23-07ee18841b1d",
                                                        "I tink I'll build a new burrow for spring",
                                                        "8ee28bdc-86e0-4b29-bb59-21a5306b7ec9"),
                                        new Message("9cc99b4b-2d9d-49f5-94b1-69f643420b42",
                                                        "New opera single just dropped! Check it out on Looney Tunes!",
                                                        "8ee28bdc-86e0-4b29-bb59-21a5306b7ec9"),
                                        new Message("54fc45f0-d060-426d-8340-2c92c8fb02dc", "Hey Baby!",
                                                        "f6be4699-ccb9-4c52-b6ae-91f5d4056fca"));

                        messageRepository.saveAll(messagesList);

                        List<Subscription> subscriptions = List.of(
                                        new Subscription("fe168b43-65a2-4414-a8e8-133a7870f8d8",
                                                        "f6be4699-ccb9-4c52-b6ae-91f5d4056fca",
                                                        "67df8454-73a8-4c99-8a69-bceec6b3e6cc"),
                                        new Subscription("e90f362b-ef86-4ea5-a49f-d79ba6059bfd",
                                                        "f6be4699-ccb9-4c52-b6ae-91f5d4056fca",
                                                        "8ee28bdc-86e0-4b29-bb59-21a5306b7ec9"),
                                        new Subscription("a85bf8fd-be88-471b-8677-6a4d9ef99305",
                                                        "7dc64326-0604-4455-807e-d0ad43a18f97",
                                                        "8ee28bdc-86e0-4b29-bb59-21a5306b7ec9"),
                                        new Subscription("8433a55a-5b5f-451e-94fa-d74def5bd417",
                                                        "7dc64326-0604-4455-807e-d0ad43a18f97",
                                                        "67df8454-73a8-4c99-8a69-bceec6b3e6cc"),
                                        new Subscription("b1fd2480-53b2-4732-8ac2-677c6cb55da2",
                                                        "8ee28bdc-86e0-4b29-bb59-21a5306b7ec9",
                                                        "67df8454-73a8-4c99-8a69-bceec6b3e6cc"),
                                        new Subscription("95ee4fe9-4321-4b17-915c-5c33464bf949",
                                                        "67df8454-73a8-4c99-8a69-bceec6b3e6cc",
                                                        "8ee28bdc-86e0-4b29-bb59-21a5306b7ec9"));
                        subscriptionRepository.saveAll(subscriptions);

                };
        }
}
