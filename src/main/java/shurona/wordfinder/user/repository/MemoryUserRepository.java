package shurona.wordfinder.user.repository;

import org.springframework.stereotype.Repository;
import shurona.wordfinder.user.domain.User;

import java.util.*;

//@Repository
public class MemoryUserRepository implements UserRepository{

    private static final Map<Long, User> store = new HashMap<>();

    private Long idSequence = 0L;

    @Override
    public Long save(User user) {
        user.setId(++idSequence);

        store.put(user.getId(), user);
        return user.getId();
    }

    @Override
    public User findById(Long userId) {
        return store.getOrDefault(userId, null);
    }

    @Override
    public Optional<User> findByNickname(String nickname) {
        return store.values().stream().filter(user -> user.getNickname().equals(nickname)).findAny();
    }

    @Override
    public Optional<User> findByLoginId(String loginId) {
        return store.values().stream().filter(user -> user.getLoginId().equals(loginId)).findAny();
    }

    @Override
    public Long[] userIds() {
        Set<Long> longs = store.keySet();
        return longs.toArray(new Long[0]);
    }

    public Optional<User> login(String loginId, String password) {

        return new ArrayList<>(store.values())
                .stream().filter(user -> user.getLoginId().equals(loginId) && user.getPassword().equals(password))
                .findFirst();
    }

    public void clearStore() {
        store.clear();
    }

}
