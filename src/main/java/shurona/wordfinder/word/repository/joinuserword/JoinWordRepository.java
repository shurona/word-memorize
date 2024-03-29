package shurona.wordfinder.word.repository.joinuserword;

import shurona.wordfinder.word.JoinWordUser;

import java.util.UUID;

public interface JoinWordRepository {
    // 유저와 단어 연결
    JoinWordUser saveUserWord(Long userId, UUID wordId);

    // 유저에게 갖고 있는 단어 ids 목록을 갖고 온다.
    JoinWordUser[] userOwnedWordList(Long userId);
}
