package be.unipartners.escqrs.cqrsquiz.events;

public interface EventStore {

    void subscribe(Subscriber subscriber);
}
