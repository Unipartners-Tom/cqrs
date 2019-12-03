package be.unipartners.escqrs.cqrsquiz.projections;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FullQuiz {

    private final UUID quizId;
    private final String quizNAme;
    private final List<QuizQuestion> questions = new ArrayList<>();
    private final String ownerName;

    public FullQuiz(UUID quizId, String quizNAme, String ownerName) {
        this.quizId = quizId;
        this.quizNAme = quizNAme;
        this.ownerName = ownerName;
    }

    public UUID getQuizId() {
        return quizId;
    }

    public String getQuizNAme() {
        return quizNAme;
    }

    public List<QuizQuestion> getQuestions() {
        return questions;
    }

    public void addQuestion(QuizQuestion quizQuestion) {
        questions.add(quizQuestion);
    }

    public String getOwnerName() {
        return ownerName;
    }

    @Override
    public String toString() {
        return "FullQuiz{" +
                "quizId=" + quizId +
                ", quizNAme='" + quizNAme + '\'' +
                ", questions=" + questions +
                ", ownerName='" + ownerName + '\'' +
                '}';
    }
}
