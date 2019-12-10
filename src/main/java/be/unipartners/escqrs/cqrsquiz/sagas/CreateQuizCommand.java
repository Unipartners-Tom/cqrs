package be.unipartners.escqrs.cqrsquiz.sagas;

import java.util.UUID;

public class CreateQuizCommand implements Command {

    private final UUID quizId;
    private final String quizName;
    private final String ownerName;

    public CreateQuizCommand(UUID quizId, String quizName, String ownerName) {
        this.quizId = quizId;
        this.quizName = quizName;
        this.ownerName = ownerName;
    }

    public UUID getQuizId() {
        return quizId;
    }

    public String getQuizName() {
        return quizName;
    }

    public String getOwnerName() {
        return ownerName;
    }
}
