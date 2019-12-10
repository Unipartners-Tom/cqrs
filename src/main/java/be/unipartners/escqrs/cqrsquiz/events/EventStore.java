package be.unipartners.escqrs.cqrsquiz.events;

import be.unipartners.escqrs.cqrsquiz.domain.events.Event;

import java.util.List;

public interface EventStore {

    void subscribe(Subscriber subscriber);

    void appendToStream(String streamID, List<Event> events);

    void appendToStream(String streamID, Event event);

    List<Event> getAllEvents();

    List<Event> getEventsByStream(String streamId);
}
