package shurona.wordfinder.repository;

import shurona.wordfinder.domain.User;

import java.util.Optional;

public interface UserRepository {
    User save(User user);

    User findById(Long userId);

    Optional<User> findByNickname(String nickname);
}
