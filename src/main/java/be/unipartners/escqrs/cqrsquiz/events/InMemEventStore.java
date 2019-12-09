package be.unipartners.escqrs.cqrsquiz.events;

import java.util.Collection;

public interface InMemEventStore extends EventStore {

    public void append(Event event);

    public void append(Collection<Event> events);

    public void trigger();
}
