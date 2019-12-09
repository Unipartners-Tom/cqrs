package be.unipartners.escqrs;

import be.unipartners.escqrs.cqrsquiz.events.InMemoryEventStore;
import be.unipartners.escqrs.cqrsquiz.events.InMemoryEventStoreImpl;
import be.unipartners.escqrs.cqrsquiz.events.QuizWasCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SpringBootApplication
public class EscqrsApplication implements CommandLineRunner {

    private String loggedInUser = "LOGGED_IN_USER";

    @Autowired
    private InMemoryEventStore eventStore;

    @Bean
    public InMemoryEventStore getInMemEventStore() {
        return new InMemoryEventStoreImpl();
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
                    } else if (input.toLowerCase().startsWith("addquiz ")) {
                        String targetName = input.substring("addquiz ".length());
                        eventStore.append(new QuizWasCreatedEvent(targetName, loggedInUser));
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
