package shurona.wordfinder.word.service;

import org.springframework.beans.factory.annotation.Autowired;
import shurona.wordfinder.word.JoinWordUser;
import shurona.wordfinder.word.Word;
import shurona.wordfinder.word.repository.joinuserword.JoinWordRepository;
import shurona.wordfinder.word.repository.word.WordRepository;

public class JoinWordUserService {

    private final WordService wordService;
    private final JoinWordRepository joinWordRepository;

    @Autowired
    public JoinWordUserService(WordService wordService, JoinWordRepository joinWordRepository) {
        this.wordService = wordService;
        this.joinWordRepository = joinWordRepository;
    }

    // 유저가 입력한 단어 생성
    JoinWordUser generate(Long userId, String wordInfo) {

        // 이미 단어가 존재하는 지 확인 한다.
        Word foundWord = this.wordService.getWordByWordInfo(wordInfo);

        // 단어가 없는 경우 저장을 한다.
        if (foundWord == null) {
            foundWord = this.wordService.saveWord(wordInfo);
        }
        return this.joinWordRepository.saveUserWord(userId, foundWord.getUid());
    }

    // 유저가 입력간 단어 목록 갖고 오기
    JoinWordUser[] getUserWordList(Long userId) {
        //
        JoinWordUser[] joinWordUsers = this.joinWordRepository.userOwnedWordList(userId);
        return joinWordUsers;
    }

}
