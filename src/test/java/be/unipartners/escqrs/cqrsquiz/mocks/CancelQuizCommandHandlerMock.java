package be.unipartners.escqrs.cqrsquiz.mocks;

import be.unipartners.escqrs.cqrsquiz.events.InMemoryEventStoreImpl;
import be.unipartners.escqrs.cqrsquiz.domain.events.QuizWasCancelledEvent;
import be.unipartners.escqrs.cqrsquiz.commands.CancelQuizCommand;
import be.unipartners.escqrs.cqrsquiz.commands.Command;
import be.unipartners.escqrs.cqrsquiz.commands.CommandHandler;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CancelQuizCommandHandlerMock implements CommandHandler {

    private final Set<UUID> alreadyProcessed = new HashSet<>();

    private final InMemoryEventStoreImpl eventStore;

    public CancelQuizCommandHandlerMock(InMemoryEventStoreImpl eventStore) {
        this.eventStore = eventStore;
    }

    @Override
    public void execute(Command command) {
        if (command instanceof CancelQuizCommand) {
            QuizWasCancelledEvent cancelEvent = new QuizWasCancelledEvent(((CancelQuizCommand) command).getQuizId());
            if(!alreadyProcessed.contains(cancelEvent.getQuizId())) {
                alreadyProcessed.add(cancelEvent.getQuizId());
                eventStore.appendToStream(cancelEvent.getQuizId().toString(), Collections.singletonList(cancelEvent));
                eventStore.trigger();
            } else {
                System.out.println("Mja " + cancelEvent.getQuizId());
            }
        }
    }
}
