package be.unipartners.escqrs.cqrsquiz.events;

import java.util.*;

// normally this should keep track of past event, but for testing purposes, we just throw them away :-)
public class InMemoryEventStore implements EventStore {

    private final Set<Subscriber> subscribers = new HashSet<>();

    private final List<Event> registeredEvents = new ArrayList<>();

    @Override
    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void append(Collection<Event> events) {
        registeredEvents.addAll(events);
    }

    public void trigger() {
        for (Event registeredEvent : registeredEvents) {
            for (Subscriber subscriber : subscribers) {
                subscriber.call(registeredEvent);
            }
        }

        registeredEvents.clear(); // normally this should keep track of past event, but for testing purposes, we just throw them away :-)
    }
}
