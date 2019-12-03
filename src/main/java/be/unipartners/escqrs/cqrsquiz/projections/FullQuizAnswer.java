package be.unipartners.escqrs.cqrsquiz.projections;

import org.springframework.util.Assert;

public class FullQuizAnswer extends Answer<FullQuiz> {

    private final FullQuiz fullQuiz;

    public FullQuizAnswer(FullQuiz fullQuiz) {
        Assert.notNull(fullQuiz);
        this.fullQuiz = fullQuiz;
    }

    @Override
    public FullQuiz getAnswer() {
        return fullQuiz;
    }
}
