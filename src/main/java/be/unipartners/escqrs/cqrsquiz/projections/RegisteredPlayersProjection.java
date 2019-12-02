package be.unipartners.escqrs.cqrsquiz.projections;

import be.unipartners.escqrs.cqrsquiz.events.Event;
import be.unipartners.escqrs.cqrsquiz.events.PlayerHasRegisteredEvent;
import be.unipartners.escqrs.cqrsquiz.queries.HowManyPlayersRegisteredQuery;
import be.unipartners.escqrs.cqrsquiz.queries.QueryObject;

import java.util.ArrayList;
import java.util.Collection;

public class RegisteredPlayersProjection implements Projection {

    private Collection<PlayerHasRegisteredEvent> inMemoryEventStore = new ArrayList<>();

    public RegisteredPlayersProjection() {
    }

    public void stream(Collection<Event> events) {
        if (events != null) {
            for (Event e : events) {
                if (e instanceof PlayerHasRegisteredEvent) {
                    inMemoryEventStore.add((PlayerHasRegisteredEvent) e);
                }
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


}
