package be.unipartners.escqrs.cqrsquiz.events;

public interface Subscriber {

    void call(Event event);
}
