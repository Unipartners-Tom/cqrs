package be.unipartners.escqrs.cqrsquiz;

import be.unipartners.escqrs.cqrsquiz.events.DayWasPassedEvent;
import be.unipartners.escqrs.cqrsquiz.events.InMemoryEventStoreImpl;
import be.unipartners.escqrs.cqrsquiz.events.QuizWasCreatedEvent;
import be.unipartners.escqrs.cqrsquiz.events.QuizWasPublishedEvent;
import be.unipartners.escqrs.cqrsquiz.mocks.CancelQuizCommandHandlerMock;
import be.unipartners.escqrs.cqrsquiz.projections.Answer;
import be.unipartners.escqrs.cqrsquiz.projections.FullQuiz;
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
        FullQuizProjection fullQuizProjection = new FullQuizProjection();
        eventStore.subscribe(fullQuizProjection);

        UUID targetuuid = UUID.randomUUID();
        String targetName = "CQRS";
        String ownerName = "ES";
        QuizWasCreatedEvent quizWasCreatedEvent = new QuizWasCreatedEvent(targetuuid, targetName, ownerName);

        DayWasPassedEvent dayWasPassedEvent1 = new DayWasPassedEvent();
        DayWasPassedEvent dayWasPassedEvent2 = new DayWasPassedEvent();

        eventStore.append(Arrays.asList(quizWasCreatedEvent, dayWasPassedEvent1, dayWasPassedEvent2));
        eventStore.trigger();
        QuizWasPublishedEvent quizWasPublishedEvent = new QuizWasPublishedEvent(targetuuid);
        eventStore.append(Collections.singletonList(quizWasPublishedEvent));
        eventStore.trigger();

        Answer<FullQuiz> theAnswer = fullQuizProjection.query(new FindSpecificFullQuizQuery(targetuuid));
        Assert.isTrue(theAnswer == null);
    }
}
