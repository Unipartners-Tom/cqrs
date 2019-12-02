package be.unipartners.escqrs.cqrsquiz.queries;

public class FindSpecificFullQuizQuery extends QueryObject {

    private final String quizName;

    public FindSpecificFullQuizQuery(String quizName) {
        this.quizName = quizName;
    }

    public String getQuizName() {
        return quizName;
    }
}
