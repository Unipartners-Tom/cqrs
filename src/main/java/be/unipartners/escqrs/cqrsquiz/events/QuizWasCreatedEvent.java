package be.unipartners.escqrs.cqrsquiz.events;

import java.util.UUID;

public class QuizWasCreatedEvent extends Event {

    private final UUID quizId;
    private final String quizName;
    private final String ownerName;

    public QuizWasCreatedEvent(UUID quizId, String quizName, String ownerName) {
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
