package shurona.wordfinder.word.repository.joinuserword;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import shurona.wordfinder.user.domain.User;
import shurona.wordfinder.word.domain.JoinWordUser;
import shurona.wordfinder.word.domain.Word;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DatabaseJoinWordRepository implements JoinWordRepository{

    @PersistenceContext
    EntityManager em;

    @Override
    public JoinWordUser findById(String id) {
        return this.em.find(JoinWordUser.class, id);
    }

    @Override
    public JoinWordUser saveUserWord(User user, Word word){
        JoinWordUser joinWordUser = new JoinWordUser(user, word);
        this.em.persist(joinWordUser);
        return joinWordUser;
    }

    @Override
    public JoinWordUser[] userOwnedWordList(Long userId) {
        String query = "select jwu from JoinWordUser as jwu where jwu.user.id = :userId";

        List<JoinWordUser> joinWordUserList = this.em.createQuery(query, JoinWordUser.class)
                .setParameter("userId", userId)
                .getResultList();

        return joinWordUserList.toArray(JoinWordUser[]::new);
    }

    @Override
    public JoinWordUser[] joinWordList() {
        String query = "select jwu from JoinWordUser as jwu";
        List<JoinWordUser> resultList = this.em.createQuery(query, JoinWordUser.class).getResultList();
        return resultList.toArray(JoinWordUser[]::new);
    }

    @Override
    public JoinWordUser[] pickListForQuiz(Long userId) {
        String query = "select jwu from JoinWordUser as jwu join fetch jwu.word where jwu.user.id = :userId " +
                "order by jwu.updatedAt desc limit 10";

        List<JoinWordUser> joinWordUserList = this.em.createQuery(query, JoinWordUser.class)
                .setParameter("userId", userId)
                .getResultList();

        return joinWordUserList.toArray(JoinWordUser[]::new);
    }

}
