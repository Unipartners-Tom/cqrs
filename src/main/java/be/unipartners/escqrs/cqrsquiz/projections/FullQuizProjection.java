package be.unipartners.escqrs.cqrsquiz.projections;

import be.unipartners.escqrs.cqrsquiz.events.Event;
import be.unipartners.escqrs.cqrsquiz.events.QuestionAddedtoQuizEvent;
import be.unipartners.escqrs.cqrsquiz.events.QuizWasCreatedEvent;
import be.unipartners.escqrs.cqrsquiz.events.QuizWasPublishedEvent;
import be.unipartners.escqrs.cqrsquiz.queries.FindSpecificFullQuizQuery;
import be.unipartners.escqrs.cqrsquiz.queries.QueryObject;

import java.util.*;

public class FullQuizProjection implements Projection {

    private final Map<UUID, FullQuiz> unpublishedQuizzesMap = new HashMap<>();
    private final Map<UUID, FullQuiz> publishedQuizzesMap = new HashMap<>();


    public void stream(Collection<Event> events) {
        if (events != null) {
            for (Event e : events) {
                if (e instanceof QuizWasCreatedEvent) {
                    if(!unpublishedQuizzesMap.containsKey(((QuizWasCreatedEvent) e).getQuizId())
                        && !publishedQuizzesMap.containsKey(((QuizWasCreatedEvent) e).getQuizId())){
                        FullQuiz newFullQuiz = new FullQuiz(
                                ((QuizWasCreatedEvent) e).getQuizId(),
                                ((QuizWasCreatedEvent) e).getQuizName(),
                                ((QuizWasCreatedEvent) e).getOwnerName()
                        );
                        unpublishedQuizzesMap.put(newFullQuiz.getQuizId(), newFullQuiz);
                    }
                } else if (e instanceof QuestionAddedtoQuizEvent) {
                    FullQuiz targetQuiz = unpublishedQuizzesMap.get(((QuestionAddedtoQuizEvent) e).getTargetQuizId());
                    if (targetQuiz != null) {
                        targetQuiz.addQuestion(new QuizQuestion(((QuestionAddedtoQuizEvent) e).getQuestion(), ((QuestionAddedtoQuizEvent) e).getAnswer()));
                    }
                } else if (e instanceof QuizWasPublishedEvent) {
                    FullQuiz quiz = unpublishedQuizzesMap.get(((QuizWasPublishedEvent) e).getQuizId());
                    if (quiz != null) {
                        publishedQuizzesMap.put(((QuizWasPublishedEvent) e).getQuizId(), quiz);
                        unpublishedQuizzesMap.remove(((QuizWasPublishedEvent) e).getQuizId());
                    }
                }
            }
        }
    }

    @Override
    public Answer query(QueryObject query) {
        if (query instanceof FindSpecificFullQuizQuery) {
            FullQuiz quiz = publishedQuizzesMap.get(((FindSpecificFullQuizQuery) query).getQuizId());
            return (quiz != null) ? new FullQuizAnswer(quiz) : null;
        }
        return null;
    }

}
