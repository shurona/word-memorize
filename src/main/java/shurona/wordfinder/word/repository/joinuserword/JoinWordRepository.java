package shurona.wordfinder.word.repository.joinuserword;

import shurona.wordfinder.word.JoinWordUser;

import java.util.UUID;

public interface JoinWordRepository {

    JoinWordUser findById(String id);
    // 유저와 단어 연결
    JoinWordUser saveUserWord(Long userId, String wordId);

    // 유저에게 갖고 있는 단어 ids 목록을 갖고 온다.
    JoinWordUser[] userOwnedWordList(Long userId);

    JoinWordUser[] joinWordList();

}
