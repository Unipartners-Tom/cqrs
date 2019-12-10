package be.unipartners.escqrs.cqrsquiz.commands;

import java.util.UUID;

public class AddQuestionToQuizCommand implements Command {

    private final UUID quizId;
    private final String question;
    private final String answer;

    public AddQuestionToQuizCommand(UUID quizId, String question, String answer) {
        this.quizId = quizId;
        this.question = question;
        this.answer = answer;
    }

    public UUID getQuizId() {
        return quizId;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}
