package be.unipartners.escqrs.cqrsquiz.sagas;

import java.util.UUID;

public class PublishQuizCommand implements Command {

    private final UUID quizId;

    public PublishQuizCommand(UUID quizId) {
        this.quizId = quizId;
    }

    public UUID getQuizId() {
        return quizId;
    }
}
