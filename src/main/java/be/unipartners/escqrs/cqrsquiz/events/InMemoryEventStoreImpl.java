package be.unipartners.escqrs.cqrsquiz.events;

import java.util.*;

// normally this should keep track of past event, but for testing purposes, we just throw them away :-)
public class InMemoryEventStoreImpl implements InMemEventStore {

    private final Set<Subscriber> subscribers = new HashSet<>();

    private final List<Event> registeredEvents = new ArrayList<>();

    @Override
    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void append(Event event) {
        if(event!=null) {
            append(Collections.singletonList(event));
        }
    }

    @Override
    public void append(Collection<Event> events) {
        registeredEvents.addAll(events);
    }

    @Override
    public void trigger() {
        List<Event> currentRegisteredEvents = new ArrayList<>(registeredEvents);
        for (Event registeredEvent : currentRegisteredEvents) {
            for (Subscriber subscriber : subscribers) {
                subscriber.call(registeredEvent);
            }
        }

        registeredEvents.clear(); // normally this should keep track of past event, but for testing purposes, we just throw them away :-)
    }
}
