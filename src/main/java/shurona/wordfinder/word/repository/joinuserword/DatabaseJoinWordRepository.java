package shurona.wordfinder.word.repository.joinuserword;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import shurona.wordfinder.word.JoinWordUser;

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
    public JoinWordUser saveUserWord(Long userId, String wordId) {;
        JoinWordUser joinWordUser = new JoinWordUser(userId, wordId, LocalDateTime.now());
        this.em.persist(joinWordUser);
        return joinWordUser;
    }

    @Override
    public JoinWordUser[] userOwnedWordList(Long userId) {
        String query = "select jwu from JoinWordUser as jwu where jwu.userId = :userId";

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
}
