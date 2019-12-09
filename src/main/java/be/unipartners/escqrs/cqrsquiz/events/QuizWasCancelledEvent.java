package be.unipartners.escqrs.cqrsquiz.events;

import java.util.UUID;

public class QuizWasCancelledEvent extends Event {

    private final UUID quizId;

    public QuizWasCancelledEvent(UUID quizId) {
        this.quizId = quizId;
    }

    public UUID getQuizId() {
        return quizId;
    }

    @Override
    public String toString() {
        return "QuizWasCancelledEvent{" +
                "quizId=" + quizId +
                '}';
    }
}
