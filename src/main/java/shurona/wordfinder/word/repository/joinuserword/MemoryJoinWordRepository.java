package shurona.wordfinder.word.repository.joinuserword;

import org.springframework.stereotype.Repository;
import shurona.wordfinder.word.JoinWordUser;

import java.util.*;

@Repository
public class MemoryJoinWordRepository implements JoinWordRepository{
    private static final Map<UUID, JoinWordUser> store = new HashMap<>();

    @Override
    public JoinWordUser saveUserWord(Long userId, UUID wordId) {
        JoinWordUser joinWordUser = new JoinWordUser();

        UUID newId = UUID.randomUUID();

        joinWordUser.setUserId(userId);
        joinWordUser.setWordId(wordId);
        joinWordUser.setId(newId);

        store.put(newId, joinWordUser);

        return store.get(newId);
    }

    @Override
    public JoinWordUser[] userOwnedWordList(Long userId) {
        Set<UUID> storeUuids = store.keySet();
        return storeUuids.stream().filter(uuid -> Objects.equals(store.get(uuid).getUserId(), userId)).map(store::get).toArray(JoinWordUser[]::new);
    }

    @Override
    public JoinWordUser[] joinWordList() {
        Set<UUID> storeUuids = store.keySet();
        return storeUuids.stream().map(store::get).toArray(JoinWordUser[]::new);
    }


}
