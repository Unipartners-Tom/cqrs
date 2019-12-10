package be.unipartners.escqrs.cqrsquiz.commands;

import java.util.UUID;

public class CancelQuizCommand implements Command {

    private final UUID quizId;

    public CancelQuizCommand(UUID quizId) {
        this.quizId = quizId;
    }

    public UUID getQuizId() {
        return quizId;
    }
}
