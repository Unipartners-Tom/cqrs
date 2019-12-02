package be.unipartners.escqrs.cqrsquiz.projections;

public class IntegerNumberAnswer extends Answer<Integer> {

    private final Integer answer;

    public IntegerNumberAnswer(Integer answer) {
        this.answer = answer;
    }

    public Integer getAnswer() {
        return answer;
    }
}
