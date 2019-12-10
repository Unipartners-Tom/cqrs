package be.unipartners.escqrs.cqrsquiz;

import be.unipartners.escqrs.cqrsquiz.domain.events.DayWasPassedEvent;
import be.unipartners.escqrs.cqrsquiz.events.InMemoryEventStoreImpl;
import be.unipartners.escqrs.cqrsquiz.domain.events.QuizWasCreatedEvent;
import be.unipartners.escqrs.cqrsquiz.domain.events.QuizWasPublishedEvent;
import be.unipartners.escqrs.cqrsquiz.mocks.CancelQuizCommandHandlerMock;
import be.unipartners.escqrs.cqrsquiz.domain.Quiz;
import be.unipartners.escqrs.cqrsquiz.projections.Answer;
import be.unipartners.escqrs.cqrsquiz.projections.FullQuizProjection;
import be.unipartners.escqrs.cqrsquiz.queries.FindSpecificFullQuizQuery;
import be.unipartners.escqrs.cqrsquiz.sagas.CommandHandler;
import be.unipartners.escqrs.cqrsquiz.sagas.QuizCreationCancellationPolicySaga;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

@SpringBootTest
public class QuizCreationCancellationPolicySagaTest {

    private InMemoryEventStoreImpl eventStore;
    private QuizCreationCancellationPolicySaga quizCreationCancellationPolicySaga;

    @BeforeEach
    void setUp() {
        eventStore = new InMemoryEventStoreImpl();
        CommandHandler mockCommandHandler = new CancelQuizCommandHandlerMock(eventStore);
        quizCreationCancellationPolicySaga = new QuizCreationCancellationPolicySaga(mockCommandHandler);
        eventStore.subscribe(quizCreationCancellationPolicySaga);
    }

    @Test
    void test_basic() {
        FullQuizProjection quizProjection = new FullQuizProjection();
        eventStore.subscribe(quizProjection);

        UUID targetuuid = UUID.randomUUID();
        String targetName = "CQRS";
        String ownerName = "ES";
        QuizWasCreatedEvent quizWasCreatedEvent = new QuizWasCreatedEvent(targetuuid, targetName, ownerName);

        DayWasPassedEvent dayWasPassedEvent1 = new DayWasPassedEvent();
        DayWasPassedEvent dayWasPassedEvent2 = new DayWasPassedEvent();

        eventStore.appendToStream(targetuuid.toString(), Arrays.asList(quizWasCreatedEvent, dayWasPassedEvent1, dayWasPassedEvent2));
        eventStore.trigger();
        QuizWasPublishedEvent quizWasPublishedEvent = new QuizWasPublishedEvent(targetuuid);
        eventStore.appendToStream(targetuuid.toString(), Collections.singletonList(quizWasPublishedEvent));
        eventStore.trigger();

        Answer<Quiz> theAnswer = quizProjection.query(new FindSpecificFullQuizQuery(targetuuid));
        Assert.isTrue(theAnswer == null);
    }
}
