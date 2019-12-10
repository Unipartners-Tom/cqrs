package be.unipartners.escqrs.cqrsquiz;

import be.unipartners.escqrs.cqrsquiz.domain.Quiz;
import be.unipartners.escqrs.cqrsquiz.domain.events.QuizWasCreatedEvent;
import be.unipartners.escqrs.cqrsquiz.domain.events.QuizWasPublishedEvent;
import be.unipartners.escqrs.cqrsquiz.events.InMemoryEventStoreImpl;
import be.unipartners.escqrs.cqrsquiz.projections.Answer;
import be.unipartners.escqrs.cqrsquiz.projections.FullQuiz;
import be.unipartners.escqrs.cqrsquiz.projections.FullQuizProjection;
import be.unipartners.escqrs.cqrsquiz.projections.RegisteredPlayersProjection;
import be.unipartners.escqrs.cqrsquiz.queries.FindSpecificFullQuizQuery;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.UUID;

@SpringBootTest
public class EventStoreTest {

    @Test
    void test_basic() {
        InMemoryEventStoreImpl eventStore = new InMemoryEventStoreImpl();

        FullQuizProjection quizProjection = new FullQuizProjection();
        RegisteredPlayersProjection registeredPlayersProjection = new RegisteredPlayersProjection();

        eventStore.subscribe(quizProjection);
        eventStore.subscribe(registeredPlayersProjection);

        UUID targetuuid = UUID.randomUUID();
        String targetName = "CQRS";
        String ownerName = "ES";
        QuizWasCreatedEvent quizWasCreatedEvent = new QuizWasCreatedEvent(targetuuid, targetName, ownerName);
        QuizWasPublishedEvent quizWasPublishedEvent = new QuizWasPublishedEvent(targetuuid);
        eventStore.appendToStream(targetuuid.toString(), Arrays.asList(quizWasCreatedEvent, quizWasPublishedEvent));


        FindSpecificFullQuizQuery query = new FindSpecificFullQuizQuery(targetuuid);
        Answer<Quiz> theNullAnswer = quizProjection.query(query);
        Assert.isTrue(theNullAnswer == null);

        eventStore.trigger();

        Answer<FullQuiz> theAnswer = quizProjection.query(query);
        Assert.isTrue(theAnswer != null);
        FullQuiz theQuiz = theAnswer.getAnswer();
        Assert.isTrue(targetName.equals(theQuiz.getQuizNAme()));
        Assert.isTrue(ownerName.equals(theQuiz.getOwnerName()));
    }

}
