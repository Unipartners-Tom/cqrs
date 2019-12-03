package be.unipartners.escqrs.cqrsquiz;

import be.unipartners.escqrs.cqrsquiz.events.Event;
import be.unipartners.escqrs.cqrsquiz.events.PlayerHasRegisteredEvent;
import be.unipartners.escqrs.cqrsquiz.events.QuizWasCreatedEvent;
import be.unipartners.escqrs.cqrsquiz.projections.RegisteredPlayersProjection;
import be.unipartners.escqrs.cqrsquiz.queries.HowManyPlayersRegisteredQuery;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest
public class RegisteredPlayersProjectionTest {

    @Test
    void test_basicProjection() {
        RegisteredPlayersProjection projection = new RegisteredPlayersProjection();

        HowManyPlayersRegisteredQuery query = new HowManyPlayersRegisteredQuery();

        Assert.isTrue(projection.query(query).getAnswer() == 0);
    }

    @Test
    void test_basicProjectionWithEvent() {
        RegisteredPlayersProjection projection = new RegisteredPlayersProjection();

        projection.stream(Collections.singletonList(new PlayerHasRegisteredEvent()));

        HowManyPlayersRegisteredQuery query = new HowManyPlayersRegisteredQuery();

        Assert.isTrue(projection.query(query).getAnswer() == 1);
    }

    @Test
    void test_basicProjection3() {
        RegisteredPlayersProjection projection = new RegisteredPlayersProjection();

        List<Event> events = new ArrayList<>();
        events.add(new PlayerHasRegisteredEvent());
        events.add(new PlayerHasRegisteredEvent());
        events.add(new PlayerHasRegisteredEvent());
        projection.stream(events);

        HowManyPlayersRegisteredQuery query = new HowManyPlayersRegisteredQuery();

        Assert.isTrue(projection.query(query).getAnswer() == 3);

        projection.stream(Collections.singletonList(new PlayerHasRegisteredEvent()));

        Assert.isTrue(projection.query(query).getAnswer() == 4);
    }

    @Test
    void test_basicProjection4() {
        RegisteredPlayersProjection projection = new RegisteredPlayersProjection();

        List<Event> events = new ArrayList<>();
        events.add(new PlayerHasRegisteredEvent());
        events.add(new QuizWasCreatedEvent(null, "", ""));
        events.add(new PlayerHasRegisteredEvent());
        projection.stream(events);

        HowManyPlayersRegisteredQuery query = new HowManyPlayersRegisteredQuery();

        Assert.isTrue(projection.query(query).getAnswer() == 2);

        projection.stream(Collections.singletonList(new QuizWasCreatedEvent(null, "", "")));

        Assert.isTrue(projection.query(query).getAnswer() == 2);
    }
}
