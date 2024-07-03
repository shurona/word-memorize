package shurona.wordfinder.word.repository.joinuserword;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import shurona.wordfinder.user.domain.User;
import shurona.wordfinder.word.domain.JoinWordUser;
import shurona.wordfinder.word.domain.Word;
import shurona.wordfinder.word.service.JoinWordUserService;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DatabaseJoinWordRepository implements JoinWordRepository {
    @PersistenceContext
    EntityManager em;

    /* ======================================================================
    Get 파트
     ======================================================================*/

    @Override
    public JoinWordUser findById(String id) {
        return this.em.find(JoinWordUser.class, id);
    }

    @Override
    public JoinWordUser findByUserWithWord(Long userId, String wordUid) {
        String query = "select jwu from JoinWordUser as jwu where jwu.user.id = :userId and jwu.word.id = :wordId";
        List<JoinWordUser> joinWordUserList = this.em.createQuery(query, JoinWordUser.class)
                .setParameter("userId", userId)
                .setParameter("wordId", wordUid)
                .getResultList();

        if (joinWordUserList.isEmpty()) {
            return null;
        }
        return joinWordUserList.get(0);
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
    public JoinWordUser[] pickListForQuiz(Long userId, int offset, int limit) {
        String query = "select jwu from JoinWordUser as jwu join fetch jwu.word where jwu.user.id = :userId " +
                "order by jwu.updatedAt desc";

        List<JoinWordUser> joinWordUserList = this.em.createQuery(query, JoinWordUser.class)
                .setParameter("userId", userId)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();

        return joinWordUserList.toArray(JoinWordUser[]::new);
    }

    @Override
    public JoinWordUser[] pickRandomForQuiz(Long userId, int offset, int limit) {
        String query = "select jwu from JoinWordUser as jwu " +
                "where jwu.id in " +
                "(select j.id from JoinWordUser as j where j.user.id = :userId order by j.updatedAt desc offset :offset)"
                + "order by random()";

//        String query = "select jwu from JoinWordUser as jwu where jwu.user.id = :userId order by jwu.updatedAt";

        List<JoinWordUser> joinWordUserList = this.em.createQuery(query, JoinWordUser.class)
                .setParameter("userId", userId)
                .setParameter("offset", offset)
                .setMaxResults(limit)
                .getResultList();
        return joinWordUserList.toArray(JoinWordUser[]::new);
    }

    /* ======================================================================
            Save 파트
             ======================================================================*/
    @Override
    public String saveUserWord(User user, Word word) {
        JoinWordUser joinWordUser = new JoinWordUser(user, word);
        this.em.persist(joinWordUser);
        return joinWordUser.getId();
    }

}
