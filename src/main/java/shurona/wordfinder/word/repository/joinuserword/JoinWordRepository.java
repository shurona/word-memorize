package shurona.wordfinder.word.repository.joinuserword;

import shurona.wordfinder.user.domain.User;
import shurona.wordfinder.word.domain.JoinWordUser;
import shurona.wordfinder.word.domain.Word;

import java.util.List;

public interface JoinWordRepository {

    JoinWordUser findById(String id);

    /**
     * ids 목록에 해당하는 JoinWordUser 목록 갖고 오기
     */
    public List<JoinWordUser> findListByIds(List<String> ids);

    /**
     * user과 word id를 기준으로 하나 갖고 온다.
     */
    JoinWordUser findByUserWithWord(Long userId, String wordUid);

    // 유저에게 갖고 있는 단어 ids 목록을 갖고 온다.
    JoinWordUser[] userOwnedWordList(Long userId);

    JoinWordUser[] joinWordList();

    JoinWordUser[] pickListForQuiz(Long userId, int offset, int limit);

    JoinWordUser[] pickRandomForQuiz(Long userId, int offset, int limit);

    int countWordUserByUserId(Long userId, boolean excludeHide);

    // 유저와 단어 연결
    String saveUserWord(User user, Word word);
}
