package be.unipartners.escqrs.cqrsquiz.mocks;

import be.unipartners.escqrs.cqrsquiz.events.Event;
import be.unipartners.escqrs.cqrsquiz.events.InMemoryEventStore;
import be.unipartners.escqrs.cqrsquiz.events.QuizWasCancelledEvent;
import be.unipartners.escqrs.cqrsquiz.sagas.CancelQuizCommand;
import be.unipartners.escqrs.cqrsquiz.sagas.Command;
import be.unipartners.escqrs.cqrsquiz.sagas.CommandHandler;

import java.util.Collections;

public class CancelQuizCommandHandlerMock implements CommandHandler {

    private final InMemoryEventStore eventStore;

    public CancelQuizCommandHandlerMock(InMemoryEventStore eventStore) {
        this.eventStore = eventStore;
    }

    @Override
    public void execute(Command command) {
        if (command instanceof CancelQuizCommand) {
            Event cancelEvent = new QuizWasCancelledEvent(((CancelQuizCommand) command).getQuizId());
            eventStore.append(Collections.singletonList(cancelEvent));
        }
    }
}
