package be.unipartners.escqrs.cqrsquiz.events;

import java.util.Collection;

public interface InMemoryEventStore extends EventStore {

    @Deprecated
    public void trigger();
}
