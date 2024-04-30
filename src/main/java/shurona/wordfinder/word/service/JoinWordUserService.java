package shurona.wordfinder.word.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shurona.wordfinder.word.JoinWordUser;
import shurona.wordfinder.word.Word;
import shurona.wordfinder.word.dto.WordListForm;
import shurona.wordfinder.word.repository.joinuserword.JoinWordRepository;
import shurona.wordfinder.word.repository.word.WordRepository;

import java.util.UUID;

@Service
public class JoinWordUserService {

    private final WordService wordService;
    private final JoinWordRepository joinWordRepository;

    @Autowired
    public JoinWordUserService(WordService wordService, JoinWordRepository joinWordRepository) {
        this.wordService = wordService;
        this.joinWordRepository = joinWordRepository;
    }

    /**
     * 유저가 입력한 단어 생성
     * @param userId 유저 아이디
     * @param wordInfo 단어 ex> name <= 이름
     */
    public JoinWordUser generate(Long userId, String wordInfo, String wordMeaning) {

        // 이미 단어가 존재하는 지 확인 한다.
        Word foundWord = this.wordService.getWordByWordInfo(wordInfo);

        // 단어가 없는 경우 저장을 한다.
        if (foundWord == null) {
            foundWord = this.wordService.saveWord(wordInfo, wordMeaning);
        }
        return this.joinWordRepository.saveUserWord(userId, foundWord.getUid());
    }

    // 유저가 입력간 단어 목록 갖고 오기
    JoinWordUser[] getUserWordList(Long userId) {
        //
        return this.joinWordRepository.userOwnedWordList(userId);
    }

    public WordListForm[] getUserWordList() {
        JoinWordUser[] joinWordUsers = this.joinWordRepository.joinWordList();

        UUID[] ids = new UUID[joinWordUsers.length];

        for (int idx = 0; idx < joinWordUsers.length; idx++) {
            ids[idx] = joinWordUsers[idx].getWordId();
        }
        Word[] wordByIds = this.wordService.getWordByIds(ids);

        WordListForm[] output = new WordListForm[wordByIds.length];

        for (int index = 0; index < wordByIds.length ; index++) {
            WordListForm wordListForm = new WordListForm();
            wordListForm.setUid(wordByIds[index].getUid());
            wordListForm.setWord(wordByIds[index].getWord());
            wordListForm.setMeaning(wordByIds[index].getMeaning());

            output[index] = wordListForm;
        }


        return output;
    }

}
