package be.unipartners.escqrs.cqrsquiz.projections;

import java.util.List;

public class FullQuiz {

    private String quizNAme;
    private List<QuizQuestion> questions;
    private String ownerName;

    public FullQuiz(String quizNAme, List<QuizQuestion> questions, String ownerName) {
        this.quizNAme = quizNAme;
        this.questions = questions;
        this.ownerName = ownerName;
    }
}
