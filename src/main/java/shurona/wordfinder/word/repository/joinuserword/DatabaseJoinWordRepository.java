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

    public List<JoinWordUser> findListByIds(List<String> ids) {
        String query = "select jwu from JoinWordUser as jwu where jwu.uid in :ids";
        return this.em.createQuery(query, JoinWordUser.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    @Override
    public JoinWordUser findByUserWithWord(Long userId, String wordUid) {
        // visible 검사는 안함
        String query = "select jwu from JoinWordUser as jwu join fetch jwu.word " +
                "where jwu.user.id = :userId and jwu.word.id = :wordId";
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
        String query = "select jwu from JoinWordUser as jwu where jwu.user.id = :userId and jwu.visible = true";

        List<JoinWordUser> joinWordUserList = this.em.createQuery(query, JoinWordUser.class)
                .setParameter("userId", userId)
                .getResultList();

        return joinWordUserList.toArray(JoinWordUser[]::new);
    }

    @Override
    public JoinWordUser[] joinWordList() {
        String query = "select jwu from JoinWordUser as jwu where jwu.visible = true";
        List<JoinWordUser> resultList = this.em.createQuery(query, JoinWordUser.class).getResultList();
        return resultList.toArray(JoinWordUser[]::new);
    }

    @Override
    public JoinWordUser[] pickListForQuiz(Long userId, int offset, int limit) {
        String query = "select jwu from JoinWordUser as jwu join fetch jwu.word where jwu.user.id = :userId and jwu.visible = true " +
                "order by jwu.lastSelectedQuiz desc";

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
                "(select j.id from JoinWordUser as j where j.user.id = :userId and j.visible = true " +
                "order by j.lastSelectedQuiz desc offset :offset)"
                + "order by random()";

//        String query = "select jwu from JoinWordUser as jwu where jwu.user.id = :userId order by jwu.updatedAt";

        List<JoinWordUser> joinWordUserList = this.em.createQuery(query, JoinWordUser.class)
                .setParameter("userId", userId)
                .setParameter("offset", offset)
                .setMaxResults(limit)
                .getResultList();
        return joinWordUserList.toArray(JoinWordUser[]::new);
    }

    public int countWordUserByUserId(Long userId, boolean excludeHide) {
        String query = "select count(jwu.id) from JoinWordUser as jwu " +
                "where jwu.user.id = :userId ";

        // 만약 hide를 포함하지 않으면
        if (excludeHide) {
            query += "and jwu.visible = true";
        }

        List<Long> resultList = this.em.createQuery(query, Long.class).setParameter("userId", userId).getResultList();
        return resultList.get(0).intValue();
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
