package shurona.wordfinder.quiz.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import shurona.wordfinder.quiz.domain.QuizDetail;
import shurona.wordfinder.quiz.domain.QuizSet;
import shurona.wordfinder.user.domain.User;
import shurona.wordfinder.word.domain.Word;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
public class DatabaseQuizRepository implements QuizRepository {
    @PersistenceContext
    EntityManager em;

    @Override
    public QuizSet saveQuizSet(QuizSet quizSet) {
        em.persist(quizSet);

        return findQuizSetById(quizSet.getId());
    }

    @Override
    public QuizSet findQuizSetById(Long id) {
        String query = "select quiz from QuizSet as quiz join fetch quiz.quizDetails where quiz.id = :id";
        List<QuizSet> ids = em.createQuery(query, QuizSet.class).setParameter("id", id).getResultList();

        if (ids.isEmpty()) {
            return null;
        } else {
            // detail sequence 맞게 정렬해준다.
            ids.get(0).getQuizDetails().sort(new Comparator<QuizDetail>() {
                @Override
                public int compare(QuizDetail o1, QuizDetail o2) {
                    return o1.getSequence() - o2.getSequence();
                }
            });
            return ids.get(0);
        }
    }

    @Override
    public QuizDetail findQuizDetailById(Long id) {
        return this.em.find(QuizDetail.class, id);
    }

    @Override
    public List<QuizSet> findQuizSetList(int pageNumber, int len, Long userId) {
        String query = "select quiz from QuizSet as quiz where quiz.user.id = :userId order by quiz.createdAt desc";
        return this.em.createQuery(query, QuizSet.class)
                .setParameter("userId", userId)
                .setFirstResult(pageNumber * len)
                .setMaxResults(len)
                .getResultList();
    }

    @Override
    public Optional<QuizSet> findRecentQuizSet(Long userId) {
        String query = "select quiz from QuizSet as quiz where quiz.user.id = :userId order by quiz.createdAt desc";
        return this.em.createQuery(query, QuizSet.class)
                .setParameter("userId", userId)
                .setMaxResults(1)
                .getResultList().stream().findAny();
    }

}
