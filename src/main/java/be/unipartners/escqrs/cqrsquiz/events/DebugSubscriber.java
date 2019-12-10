package be.unipartners.escqrs.cqrsquiz.events;

import be.unipartners.escqrs.cqrsquiz.domain.events.Event;

public class DebugSubscriber implements Subscriber {
    @Override
    public void call(Event event) {
        System.out.println("# -> received Event " + event.toString());
    }
}
