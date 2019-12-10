package be.unipartners.escqrs.cqrsquiz.events;

import be.unipartners.escqrs.cqrsquiz.domain.events.Event;

public interface Subscriber {

    void call(Event event);
}
