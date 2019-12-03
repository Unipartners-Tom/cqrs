package be.unipartners.escqrs.cqrsquiz.sagas;

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
