package be.unipartners.escqrs;

import be.unipartners.escqrs.cqrsquiz.events.*;
import be.unipartners.escqrs.cqrsquiz.sagas.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

@SpringBootApplication
public class EscqrsApplication implements CommandLineRunner {

    private String loggedInUser = "LOGGED_IN_USER";

    @Autowired
    private InMemoryEventStore eventStore;

    @Autowired
    private QuizUseCases quizUseCases;


    @Bean
    public InMemoryEventStore getInMemEventStore() {
        return new InMemoryEventStoreImpl();
    }

    @Bean
    public Subscriber getDebugSubscriber() {
        Subscriber debugSubscriber =  new DebugSubscriber();
        eventStore.subscribe(debugSubscriber);
        return debugSubscriber;
    }

    @Bean
    public QuizUseCases getQuizUseCases() {
        QuizUseCases quizUseCases = new QuizUseCases(eventStore);
        return quizUseCases;
    }



    public static void main(String[] args) {
        SpringApplication.run(EscqrsApplication.class, args);
    }

    public boolean doSomething() {
        return true;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(eventStore != null ? "eventStore is not null" : "eventStore is null");
        Boolean running = true;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(System.in));
            while (running) {
                System.out.print("Enter something : ");
                String input = br.readLine();

                if (!StringUtils.isEmpty(input)) {
                    if ("q".equalsIgnoreCase(input) || "quit".equalsIgnoreCase(input)) {
                        System.out.println("Exit!");
                        running = false;
                    } else if ("trigger".equalsIgnoreCase(input) || "publish".equalsIgnoreCase(input)) {
                        eventStore.trigger();
                    } else if (input.toLowerCase().startsWith("addquiz ")) {
                        String targetName = input.substring("addquiz ".length());
                        UUID newUUID = UUID.randomUUID();
                        System.out.println(">>> Creating quiz with id " + newUUID);
                        quizUseCases.execute(new CreateQuizCommand(newUUID, targetName, loggedInUser));
                        // eventStore.append(new QuizWasCreatedEvent(newUUID, targetName, loggedInUser));
                    } else if (input.toLowerCase().startsWith("cancelquiz ")) {
                        String targetuuid = input.substring("cancelquiz ".length());
                        quizUseCases.execute(new CancelQuizCommand(UUID.fromString(targetuuid)));
                        // eventStore.append(new QuizWasCancelledEvent(UUID.fromString(targetuuid)));
                    } else if (input.toLowerCase().startsWith("publishquiz ")) {
                        String targetuuid = input.substring("publishquiz ".length());
                        quizUseCases.execute(new PublishQuizCommand(UUID.fromString(targetuuid)));
                        // eventStore.append(new QuizWasPublishedEvent(UUID.fromString(targetuuid)));
                    }  else if (input.toLowerCase().startsWith("addquestion ")) {
                        String relevantInput = input.substring("addquestion ".length());
                        String[] relevantInputParts = relevantInput.split(" ");
                        if(relevantInputParts.length == 3) {
                            quizUseCases.execute(new AddQuestionToQuizCommand(UUID.fromString(relevantInputParts[0]), relevantInputParts[1], relevantInputParts[2]));
                            // eventStore.append(new QuestionAddedtoQuizEvent(UUID.fromString(relevantInputParts[0]), relevantInputParts[1], relevantInputParts[2]));
                        }
                    } else if (input.equalsIgnoreCase("nextday")) {
                        // eventStore.append(new DayWasPassedEvent());
                    }
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
