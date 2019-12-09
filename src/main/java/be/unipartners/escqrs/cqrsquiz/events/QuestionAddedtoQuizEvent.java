package be.unipartners.escqrs.cqrsquiz.events;

import java.util.UUID;

public class QuestionAddedtoQuizEvent extends Event {

    private final UUID targetQuizId;
    private final String question;
    private final String answer;

    public QuestionAddedtoQuizEvent(UUID targetQuizId, String question, String answer) {
        this.targetQuizId = targetQuizId;
        this.question = question;
        this.answer = answer;
    }

    public UUID getTargetQuizId() {
        return targetQuizId;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    @Override
    public String toString() {
        return "QuestionAddedtoQuizEvent{" +
                "targetQuizId=" + targetQuizId +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}
