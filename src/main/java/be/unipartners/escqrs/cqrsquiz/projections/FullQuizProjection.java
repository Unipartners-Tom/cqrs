package be.unipartners.escqrs.cqrsquiz.projections;

import be.unipartners.escqrs.cqrsquiz.events.*;
import be.unipartners.escqrs.cqrsquiz.queries.FindSpecificFullQuizQuery;
import be.unipartners.escqrs.cqrsquiz.queries.QueryObject;

import java.util.*;

public class FullQuizProjection implements Projection, Subscriber {

    private final Map<UUID, FullQuiz> unpublishedQuizzesMap = new HashMap<>();
    private final Map<UUID, FullQuiz> publishedQuizzesMap = new HashMap<>();

    @Deprecated
    public void stream(Collection<Event> events) {
        if (events != null) {
            for (Event e : events) {
                call(e);
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

    @Override
    public void call(Event event) {
        if (event instanceof QuizWasCreatedEvent) {
            if(!unpublishedQuizzesMap.containsKey(((QuizWasCreatedEvent) event).getQuizId())
                    && !publishedQuizzesMap.containsKey(((QuizWasCreatedEvent) event).getQuizId())){
                FullQuiz newFullQuiz = new FullQuiz(
                        ((QuizWasCreatedEvent) event).getQuizId(),
                        ((QuizWasCreatedEvent) event).getQuizName(),
                        ((QuizWasCreatedEvent) event).getOwnerName()
                );
                unpublishedQuizzesMap.put(newFullQuiz.getQuizId(), newFullQuiz);
            }
        } else if (event instanceof QuestionAddedtoQuizEvent) {
            FullQuiz targetQuiz = unpublishedQuizzesMap.get(((QuestionAddedtoQuizEvent) event).getTargetQuizId());
            if (targetQuiz != null) {
                targetQuiz.addQuestion(new QuizQuestion(((QuestionAddedtoQuizEvent) event).getQuestion(), ((QuestionAddedtoQuizEvent) event).getAnswer()));
            }
        } else if (event instanceof QuizWasPublishedEvent) {
            FullQuiz quiz = unpublishedQuizzesMap.get(((QuizWasPublishedEvent) event).getQuizId());
            if (quiz != null) {
                publishedQuizzesMap.put(((QuizWasPublishedEvent) event).getQuizId(), quiz);
                unpublishedQuizzesMap.remove(((QuizWasPublishedEvent) event).getQuizId());
            }
        }
    }
}
