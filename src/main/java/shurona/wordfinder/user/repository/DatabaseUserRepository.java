package shurona.wordfinder.user.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import shurona.wordfinder.user.domain.User;

import java.util.List;
import java.util.Optional;

//@Repository
public class DatabaseUserRepository implements UserRepository {

    @PersistenceContext
    EntityManager em;

    @Override
    public User save(User user) {
        em.persist(user);

        return findById(user.getId());
    }

    @Override
    public User findById(Long userId) {
        return em.find(User.class, userId);
    }

    @Override
    public Optional<User> findByNickname(String nickname) {
        String jpqlQuery = "select user from User as user where user.nickname = :nickname";

        List<User> userInfo = this.em.createQuery(jpqlQuery, User.class)
                .setParameter("nickname", nickname)
                .getResultList();

        if (!userInfo.isEmpty()) {
            return Optional.of(userInfo.get(0));
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByLoginId(String loginId) {
        String jpqlQuery = "select user from User as user where user.loginId = :loginId";

        List<User> userInfo = this.em.createQuery(jpqlQuery, User.class)
                .setParameter("loginId", loginId)
                .getResultList();

        if (!userInfo.isEmpty()) {
            return Optional.of(userInfo.get(0));
        }
        return Optional.empty();
    }

    @Override
    public Long[] userIds() {
        String jpqlQuery = "select user.id from User as user";

        List<Long> userIds = this.em.createQuery(jpqlQuery, Long.class).getResultList();

        return userIds.toArray(Long[]::new);
    }

    @Override
    public Optional<User> login(String loginId, String password) {
        String jpqlQuery = "select user from User as user where user.loginId = :loginId and user.password = :password";

        List<User> userInfo = this.em.createQuery(jpqlQuery, User.class)
                .setParameter("loginId", loginId)
                .setParameter("password", password)
                .getResultList();

        if (!userInfo.isEmpty()) {
            return Optional.of(userInfo.get(0));
        }
        return Optional.empty();
    }
}
