package be.unipartners.escqrs.cqrsquiz.sagas;

public interface CommandHandler {
    void execute(Command command);
}
