package shurona.wordfinder.user.repository;

import shurona.wordfinder.user.domain.User;

import java.util.Optional;

public interface UserRepository {
    User save(User user);

    User findById(Long userId);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByLoginId(String loginId);

    public Long[] userIds();

    Optional<User> login(String loginId, String password);

}
