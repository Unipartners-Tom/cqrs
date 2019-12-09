package be.unipartners.escqrs.cqrsquiz.events;

import java.util.UUID;

public class QuizWasPublishedEvent extends Event {

    private final UUID quizId;

    public QuizWasPublishedEvent(UUID quizId) {
        this.quizId = quizId;
    }

    public UUID getQuizId() {
        return quizId;
    }

    @Override
    public String toString() {
        return "QuizWasPublishedEvent{" +
                "quizId=" + quizId +
                '}';
    }
}
