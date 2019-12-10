package be.unipartners.escqrs.cqrsquiz.sagas;

import be.unipartners.escqrs.cqrsquiz.domain.Quiz;
import be.unipartners.escqrs.cqrsquiz.domain.events.Event;
import be.unipartners.escqrs.cqrsquiz.domain.utils.AggregateIdGenerator;
import be.unipartners.escqrs.cqrsquiz.events.EventStore;

import java.util.List;

public class QuizUseCases implements CommandHandler {

    private final  EventStore eventStore;

    public QuizUseCases(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    @Override
    public void execute(Command command) {
        if(command instanceof CreateQuizCommand) {
            executeCreateQuizCommand((CreateQuizCommand) command);
        } else if(command instanceof CancelQuizCommand) {
            executeCancelQuizCommand((CancelQuizCommand) command);
        } else if(command instanceof AddQuestionToQuizCommand) {
            executeAddQuestionToQuizCommand((AddQuestionToQuizCommand) command);
        } else if(command instanceof PublishQuizCommand) {
            executePublishQuizCommand((PublishQuizCommand) command);
        }
    }

    private void executePublishQuizCommand(PublishQuizCommand command) {
        String aggregateId = AggregateIdGenerator.generateAggragateId(command.getQuizId(), Quiz.class);
        List<Event> eventsByStream = eventStore.getEventsByStream(aggregateId);
        List<Event> newEventsToSave = new Quiz(eventsByStream).publishQuiz(command);
        this.eventStore.appendToStream(aggregateId, newEventsToSave);
    }

    private void executeAddQuestionToQuizCommand(AddQuestionToQuizCommand command) {
        String aggregateId = AggregateIdGenerator.generateAggragateId(command.getQuizId(), Quiz.class);
        List<Event> eventsByStream = eventStore.getEventsByStream(aggregateId);
        List<Event> newEventsToSave = new Quiz(eventsByStream).addQuestionToQuiz(command);
        this.eventStore.appendToStream(aggregateId, newEventsToSave);
    }

    private void executeCancelQuizCommand(CancelQuizCommand command) {
        String aggregateId = AggregateIdGenerator.generateAggragateId(command.getQuizId(), Quiz.class);
        List<Event> eventsByStream = eventStore.getEventsByStream(aggregateId);
        List<Event> newEventsToSave = new Quiz(eventsByStream).cancelQuiz(command);
        this.eventStore.appendToStream(aggregateId, newEventsToSave);
    }

    private void executeCreateQuizCommand(CreateQuizCommand command) {
        String aggregateId = AggregateIdGenerator.generateAggragateId(command.getQuizId(), Quiz.class);
        List<Event> eventsByStream = eventStore.getEventsByStream(aggregateId);
        List<Event> newEventsToSave = new Quiz(eventsByStream).createQuiz(command);
        this.eventStore.appendToStream(aggregateId, newEventsToSave);
    }
}
