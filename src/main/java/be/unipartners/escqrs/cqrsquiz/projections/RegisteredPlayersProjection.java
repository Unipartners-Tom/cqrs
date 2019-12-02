package be.unipartners.escqrs.cqrsquiz.projections;

import be.unipartners.escqrs.cqrsquiz.events.Event;
import be.unipartners.escqrs.cqrsquiz.events.PlayerRegisteredEvent;
import be.unipartners.escqrs.cqrsquiz.queries.HowManyPlayersRegisteredQuery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RegisteredPlayersProjection implements Projection {

    private Collection<PlayerRegisteredEvent> inMemoryEventStore = new ArrayList<>();

    public RegisteredPlayersProjection() {
    }

    public int query(HowManyPlayersRegisteredQuery query) {
        return inMemoryEventStore.size();
    }

    public void stream(Collection<Event> events) {
        if (events != null) {
            List<PlayerRegisteredEvent> collectedEvents = new ArrayList<>();
            for (Event e : events) {
                if (e instanceof PlayerRegisteredEvent) {
                    collectedEvents.add((PlayerRegisteredEvent) e);
                }
            }
            inMemoryEventStore.addAll(collectedEvents);
        }
    }
}