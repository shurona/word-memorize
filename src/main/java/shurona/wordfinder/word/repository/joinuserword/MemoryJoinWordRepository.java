package shurona.wordfinder.word.repository.joinuserword;

import shurona.wordfinder.user.domain.User;
import shurona.wordfinder.word.domain.JoinWordUser;
import shurona.wordfinder.word.domain.Word;

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
    public String saveUserWord(User user, Word word) {
        JoinWordUser joinWordUser = new JoinWordUser(user, word);
        String newId = UUID.randomUUID().toString();
        joinWordUser.setId(newId);
        store.put(newId, joinWordUser);
        return store.get(newId).getId();
    }

    @Override
    public JoinWordUser[] userOwnedWordList(Long userId) {
        Set<String> storeUuids = store.keySet();

        for (String storeUuid : storeUuids) {
            System.out.println(store.get(storeUuid).getUser());
            break;
        }

        return storeUuids.stream().filter(uuid -> Objects.equals(store.get(uuid).getUser().getId(), userId)).map(store::get).toArray(JoinWordUser[]::new);
    }

    @Override
    public JoinWordUser[] joinWordList() {
        Set<String> storeUuids = store.keySet();
        return storeUuids.stream().map(store::get).toArray(JoinWordUser[]::new);
    }


    public void clearStore() {
        store.clear();
    }

    /*
    여기서 부터 미구현
     */

    @Override
    public JoinWordUser[] pickListForQuiz(Long userId, int offset, int limit) {
        return new JoinWordUser[0];
    }

    @Override
    public JoinWordUser findByUserWithWord(Long userId, String wordUid) {
        return null;
    }

    @Override
    public JoinWordUser[] pickRandomForQuiz(Long userId, int offset, int limit) {
        return new JoinWordUser[0];
    }

    @Override
    public List<JoinWordUser> findListByIds(List<String> ids) {
        return null;
    }

    @Override
    public int countWordUserByUserId(Long userId, boolean excludeHide) {
        return 0;
    }
}
