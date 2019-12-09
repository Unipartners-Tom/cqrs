package be.unipartners.escqrs.cqrsquiz.sagas;

import be.unipartners.escqrs.cqrsquiz.events.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class QuizCreationCancellationPolicySaga implements Subscriber {

    private Map<UUID, Integer> inMemoryPlaceholder = new HashMap<>(); // counting daysHavePassed for a specific created Quiz (also removing when published)

    private final CommandHandler commandHandler;

    public QuizCreationCancellationPolicySaga(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void call(Event event) {
        if (event instanceof QuizWasCreatedEvent && !inMemoryPlaceholder.containsKey(((QuizWasCreatedEvent) event).getQuizId())) {
            inMemoryPlaceholder.put(((QuizWasCreatedEvent) event).getQuizId(), 0);
        } else if (event instanceof DayWasPassedEvent) {
            for (UUID uuid : inMemoryPlaceholder.keySet()) {
                Integer newValue = inMemoryPlaceholder.get(uuid) + 1;
                if (newValue < 2) {
                    inMemoryPlaceholder.put(uuid, newValue);
                } else {
                    Command cancelCommand = new CancelQuizCommand(uuid);
                    commandHandler.execute(cancelCommand);
                }
            }
        } else if (event instanceof QuizWasPublishedEvent) {
            inMemoryPlaceholder.remove(((QuizWasPublishedEvent) event).getQuizId());
        }
    }
}
