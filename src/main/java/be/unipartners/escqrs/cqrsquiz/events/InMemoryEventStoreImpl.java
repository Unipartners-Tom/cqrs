package be.unipartners.escqrs.cqrsquiz.events;

import be.unipartners.escqrs.cqrsquiz.domain.events.Event;
import org.springframework.util.StringUtils;

import java.util.*;

// normally this should keep track of past event, but for testing purposes, we just throw them away :-)
public class InMemoryEventStoreImpl implements InMemoryEventStore {

    private final Set<Subscriber> subscribers = new HashSet<>();

    private final List<Event> registeredEvents = new ArrayList<>();

    private final List<Event> registeredUntriggeredEvents = new ArrayList<>();

    private final Map<String, List<Event>> eventStreams = new HashMap<>();

    @Override
    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void appendToStream(String streamID, Event event) {
        appendToStream(streamID, Collections.singletonList(event));
    }

    @Override
    public void appendToStream(String streamID, List<Event> events) {
        if (StringUtils.isEmpty(streamID)) {
            throw new IllegalArgumentException("Dat gaat niet");
        }

        if (eventStreams.containsKey(streamID)) {
            eventStreams.get(streamID).addAll(events);
        } else {
            eventStreams.put(streamID, events);
        }
        registeredEvents.addAll(events);
        registeredUntriggeredEvents.addAll(events);
    }

    @Override
    public List<Event> getAllEvents() {
        return new ArrayList<>(registeredEvents);
    }

    @Override
    public List<Event> getEventsByStream(String streamId) {
        return (eventStreams.get(streamId) != null) ? new ArrayList<>(eventStreams.get(streamId)) : Collections.emptyList();
    }

    @Override
    @Deprecated
    public void trigger() {
        List<Event> currentRegisteredEvents = new ArrayList<>(registeredUntriggeredEvents);
        for (Event registeredEvent : currentRegisteredEvents) {
            for (Subscriber subscriber : subscribers) {
                subscriber.call(registeredEvent);
            }
        }

        registeredUntriggeredEvents.clear(); // normally this should keep track of past event, but for testing purposes, we just throw them away :-)
    }
}
