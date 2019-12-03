package be.unipartners.escqrs.cqrsquiz;

import be.unipartners.escqrs.cqrsquiz.events.InMemoryEventStore;
import be.unipartners.escqrs.cqrsquiz.events.QuizWasCreatedEvent;
import be.unipartners.escqrs.cqrsquiz.events.QuizWasPublishedEvent;
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
        InMemoryEventStore eventStore = new InMemoryEventStore();

        FullQuizProjection fullQuizProjection = new FullQuizProjection();
        RegisteredPlayersProjection registeredPlayersProjection = new RegisteredPlayersProjection();

        eventStore.subscribe(fullQuizProjection);
        eventStore.subscribe(registeredPlayersProjection);

        UUID targetuuid = UUID.randomUUID();
        String targetName = "CQRS";
        String ownerName = "ES";
        QuizWasCreatedEvent quizWasCreatedEvent = new QuizWasCreatedEvent(targetuuid, targetName, ownerName);
        QuizWasPublishedEvent quizWasPublishedEvent = new QuizWasPublishedEvent(targetuuid);
        eventStore.append(Arrays.asList(quizWasCreatedEvent, quizWasPublishedEvent));


        FindSpecificFullQuizQuery query = new FindSpecificFullQuizQuery(targetuuid);
        Answer<FullQuiz> theNullAnswer = fullQuizProjection.query(query);
        Assert.isTrue(theNullAnswer == null);

        eventStore.trigger();

        Answer<FullQuiz> theAnswer = fullQuizProjection.query(query);
        Assert.isTrue(theAnswer != null);
        FullQuiz theFullQuiz = theAnswer.getAnswer();
        Assert.isTrue(targetName.equals(theFullQuiz.getQuizNAme()));
        Assert.isTrue(ownerName.equals(theFullQuiz.getOwnerName()));
    }

}
