package be.unipartners.escqrs.cqrsquiz.queries;

import java.util.UUID;

public class FindSpecificFullQuizQuery extends QueryObject {

    private final UUID quizId;

    public FindSpecificFullQuizQuery(UUID quizId) {
        this.quizId = quizId;
    }

    public UUID getQuizId() {
        return quizId;
    }
}
