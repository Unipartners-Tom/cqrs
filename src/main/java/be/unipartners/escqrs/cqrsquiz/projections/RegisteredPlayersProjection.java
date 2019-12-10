package be.unipartners.escqrs.cqrsquiz.projections;

import be.unipartners.escqrs.cqrsquiz.domain.events.Event;
import be.unipartners.escqrs.cqrsquiz.domain.events.PlayerHasRegisteredEvent;
import be.unipartners.escqrs.cqrsquiz.events.Subscriber;
import be.unipartners.escqrs.cqrsquiz.queries.HowManyPlayersRegisteredQuery;
import be.unipartners.escqrs.cqrsquiz.queries.QueryObject;

import java.util.ArrayList;
import java.util.Collection;

public class RegisteredPlayersProjection implements Projection, Subscriber {

    private Collection<PlayerHasRegisteredEvent> inMemoryEventStore = new ArrayList<>();

    public RegisteredPlayersProjection() {
    }

    @Deprecated
    public void stream(Collection<Event> events) {
        if (events != null) {
            for (Event e : events) {
                call(e);
            }
        }
    }

    @Override
    public Answer<Integer> query(QueryObject query) {
        if (query instanceof HowManyPlayersRegisteredQuery) {
            return howManyPlayersRegisteredQueryQuery();
        } // else
        return null;
    }

    private Answer<Integer> howManyPlayersRegisteredQueryQuery() {
        return new IntegerNumberAnswer(inMemoryEventStore.size());
    }


    @Override
    public void call(Event event) {
        if (event instanceof PlayerHasRegisteredEvent) {
            inMemoryEventStore.add((PlayerHasRegisteredEvent) event);
        }
    }
}
