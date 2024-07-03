package shurona.wordfinder.word.repository.joinuserword;

import shurona.wordfinder.user.domain.User;
import shurona.wordfinder.word.domain.JoinWordUser;
import shurona.wordfinder.word.domain.Word;

public interface JoinWordRepository {

    JoinWordUser findById(String id);

    JoinWordUser findByUserWithWord(Long userId, String wordUid);

    // 유저에게 갖고 있는 단어 ids 목록을 갖고 온다.
    JoinWordUser[] userOwnedWordList(Long userId);

    JoinWordUser[] joinWordList();

    JoinWordUser[] pickListForQuiz(Long userId, int offset, int limit);

    JoinWordUser[] pickRandomForQuiz(Long userId, int offset, int limit);

    // 유저와 단어 연결
    String saveUserWord(User user, Word word);
}
