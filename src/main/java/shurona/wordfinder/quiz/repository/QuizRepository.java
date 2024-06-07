package shurona.wordfinder.quiz.repository;

import shurona.wordfinder.quiz.domain.QuizDetail;
import shurona.wordfinder.quiz.domain.QuizSet;
import shurona.wordfinder.user.domain.User;
import shurona.wordfinder.word.domain.Word;

import java.util.List;

public interface QuizRepository {
    // QuizSet detail 저장
    public QuizSet saveQuizSet(QuizSet quizSet);

    public QuizSet findQuizSetById(Long id);

    public QuizDetail findQuizDetailById(Long id);

    public List<QuizSet> findQuizSetList(int pageNumber, int len, Long userId);
}
