package shurona.wordfinder.user.repository;

import shurona.wordfinder.user.User;

import java.util.Optional;

public interface UserRepository {
    User save(User user);

    User findById(Long userId);

    Optional<User> findByNickname(String nickname);
}
