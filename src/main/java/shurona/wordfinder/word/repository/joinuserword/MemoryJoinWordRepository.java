package shurona.wordfinder.word.repository.joinuserword;

import shurona.wordfinder.word.domain.JoinWordUser;

import java.time.LocalDateTime;
import java.util.*;

//@Repository
public class MemoryJoinWordRepository implements JoinWordRepository{
    private static final Map<String, JoinWordUser> store = new HashMap<>();

    @Override
    public JoinWordUser findById(String id) {
        return store.get(id);
    }

    @Override
    public JoinWordUser saveUserWord(Long userId, String wordId) {
        JoinWordUser joinWordUser = new JoinWordUser(userId, wordId, LocalDateTime.now());
        String newId = UUID.randomUUID().toString();
        joinWordUser.setId(newId);
        store.put(newId, joinWordUser);
        return store.get(newId);
    }

    @Override
    public JoinWordUser[] userOwnedWordList(Long userId) {
        Set<String> storeUuids = store.keySet();
        return storeUuids.stream().filter(uuid -> Objects.equals(store.get(uuid).getUserId(), userId)).map(store::get).toArray(JoinWordUser[]::new);
    }

    @Override
    public JoinWordUser[] joinWordList() {
        Set<String> storeUuids = store.keySet();
        return storeUuids.stream().map(store::get).toArray(JoinWordUser[]::new);
    }

    public void clearStore() {
        store.clear();
    }

}
