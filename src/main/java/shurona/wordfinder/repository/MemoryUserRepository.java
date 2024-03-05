package shurona.wordfinder.repository;

import org.springframework.stereotype.Repository;
import shurona.wordfinder.domain.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class MemoryUserRepository implements UserRepository{

    private static final Map<Long, User> store = new HashMap<>();

    private Long idSequence = 0L;

    @Override
    public User save(User user) {
        user.setId(++idSequence);

        store.put(user.getId(), user);
        return user;
    }

    @Override
    public User findById(Long userId) {
        return store.getOrDefault(userId, null);
    }

    @Override
    public Optional<User> findByNickname(String nickname) {
        return store.values().stream().filter(user -> user.getNickname().equals(nickname)).findAny();
    }
}
