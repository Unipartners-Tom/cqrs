package be.unipartners.escqrs.cqrsquiz.commands;

public interface CommandHandler {
    void execute(Command command);
}
