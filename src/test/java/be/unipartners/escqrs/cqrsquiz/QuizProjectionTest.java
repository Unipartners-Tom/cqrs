package be.unipartners.escqrs.cqrsquiz;

import be.unipartners.escqrs.cqrsquiz.domain.Quiz;
import be.unipartners.escqrs.cqrsquiz.domain.events.QuizWasCreatedEvent;
import be.unipartners.escqrs.cqrsquiz.domain.events.QuizWasPublishedEvent;
import be.unipartners.escqrs.cqrsquiz.projections.Answer;
import be.unipartners.escqrs.cqrsquiz.projections.FullQuiz;
import be.unipartners.escqrs.cqrsquiz.projections.FullQuizProjection;
import be.unipartners.escqrs.cqrsquiz.queries.FindSpecificFullQuizQuery;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

@SpringBootTest
public class QuizProjectionTest {

    @Test
    void test_basicProjection() {
        FullQuizProjection projection = new FullQuizProjection();

        UUID targetuuid = UUID.randomUUID();
        FindSpecificFullQuizQuery query = new FindSpecificFullQuizQuery(targetuuid);

        Assert.isTrue(projection.query(query) == null);
    }

    @Test
    void test_basicProjectionWithoutPublish() {
        FullQuizProjection projection = new FullQuizProjection();

        UUID targetuuid = UUID.randomUUID();
        String targetName = "CQRS";
        String ownerName = "ES";
        QuizWasCreatedEvent event = new QuizWasCreatedEvent(targetuuid, targetName, ownerName);
        projection.stream(Collections.singletonList(event));

        FindSpecificFullQuizQuery query = new FindSpecificFullQuizQuery(targetuuid);

        Answer<Quiz> theAnswer = projection.query(query);
        Assert.isTrue(theAnswer == null);
    }

    @Test
    void test_basicProjectionWithPublish() {
        FullQuizProjection projection = new FullQuizProjection();

        UUID targetuuid = UUID.randomUUID();
        String targetName = "CQRS";
        String ownerName = "ES";
        QuizWasCreatedEvent quizWasCreatedEvent = new QuizWasCreatedEvent(targetuuid, targetName, ownerName);
        QuizWasPublishedEvent quizWasPublishedEvent = new QuizWasPublishedEvent(targetuuid);
        projection.stream(Arrays.asList(quizWasCreatedEvent, quizWasPublishedEvent));

        FindSpecificFullQuizQuery query = new FindSpecificFullQuizQuery(targetuuid);

        Answer<FullQuiz> theAnswer = projection.query(query);
        Assert.isTrue(theAnswer != null);
        FullQuiz theQuiz = theAnswer.getAnswer();
        Assert.isTrue(targetName.equals(theQuiz.getQuizNAme()));
        Assert.isTrue(ownerName.equals(theQuiz.getOwnerName()));
    }

}
