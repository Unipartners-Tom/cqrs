package be.unipartners.escqrs.cqrsquiz.events;

public class DebugSubscriber implements Subscriber {
    @Override
    public void call(Event event) {
        System.out.println("# -> received Event " + event.toString());
    }
}
