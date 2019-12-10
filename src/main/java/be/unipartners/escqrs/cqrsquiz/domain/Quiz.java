package be.unipartners.escqrs.cqrsquiz.domain;

import be.unipartners.escqrs.cqrsquiz.commands.AddQuestionToQuizCommand;
import be.unipartners.escqrs.cqrsquiz.commands.CancelQuizCommand;
import be.unipartners.escqrs.cqrsquiz.commands.CreateQuizCommand;
import be.unipartners.escqrs.cqrsquiz.commands.PublishQuizCommand;
import be.unipartners.escqrs.cqrsquiz.domain.events.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Quiz {

    private final List<Event> events;

    private UUID quizId;

    public Quiz(List<Event> events) {
        this.events = events;
        this.quizId = ((QuizWasCreatedEvent) events.get(0)).getQuizId(); // class cast exception if not correct -> is there a better way..?
    }

    public List<Event> cancelQuiz(CancelQuizCommand command) {
        if (command == null || !quizId.equals(command.getQuizId())) {
            throw new IllegalArgumentException("Jammer");
        } else if (isAlreadyPublished()) {
            throw new IllegalStateException("Jammer toch, da mag niemeer");
        }
        events.add(new QuizWasCancelledEvent(this.quizId));
        return new ArrayList<>(events);
    }

    public List<Event> createQuiz(CreateQuizCommand command) {
        if (events.isEmpty()) {
            events.add(new QuizWasCreatedEvent(command.getQuizId(), command.getQuizName(), command.getOwnerName()));
        } else {
            throw new IllegalStateException("Iets aanmaken wat al bestaat is nie de bedoeling");
        }
        return new ArrayList<>(events);
    }

    public List<Event> addQuestionToQuiz(AddQuestionToQuizCommand command) {
        if (events.isEmpty()) {
            throw new IllegalStateException("Gaat nie");
        } else if (isInAnEndState()) {
            throw new IllegalStateException("Jammer toch, da mag nie");
        }
        events.add(new QuestionAddedtoQuizEvent(command.getQuizId(), command.getQuestion(), command.getAnswer()));
        return new ArrayList<>(events);
    }

    public List<Event> publishQuiz(PublishQuizCommand command) {
        if (command == null || !quizId.equals(command.getQuizId())) {
            throw new IllegalArgumentException("Jammer");
        } else if (!hasAtLeast1Question()) {
            throw new IllegalStateException("Jammer toch");
        } else if (isAlreadyCancelled()) {
            throw new IllegalStateException("Jammer toch, da mag niemeer");
        }
        events.add(new QuizWasPublishedEvent(this.quizId));
        return new ArrayList<>(events);
    }

    private boolean hasAtLeast1Question() {
        return events.stream().anyMatch(e -> e instanceof QuestionAddedtoQuizEvent);
    }

    private boolean isAlreadyPublished() {
        return events.stream().anyMatch(e -> e instanceof QuizWasPublishedEvent);
    }

    private boolean isAlreadyCancelled() {
        return events.stream().anyMatch(e -> e instanceof QuizWasCancelledEvent);
    }

    private boolean isInAnEndState() {
        return isAlreadyPublished() || isAlreadyCancelled();
    }
}
