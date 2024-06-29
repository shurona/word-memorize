package shurona.wordfinder.word.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shurona.wordfinder.user.domain.User;
import shurona.wordfinder.user.repository.UserRepository;
import shurona.wordfinder.user.service.UserService;
import shurona.wordfinder.word.domain.JoinWordUser;
import shurona.wordfinder.word.domain.Word;
import shurona.wordfinder.word.domain.WordEditStatus;
import shurona.wordfinder.word.dto.WordListForm;
import shurona.wordfinder.word.repository.joinuserword.JoinWordRepository;

@Service
@Transactional(readOnly = true)
public class JoinWordUserService {

    private final WordService wordService;
    private final JoinWordRepository joinWordRepository;
    private final UserService userService;

    @Autowired
    public JoinWordUserService(WordService wordService, UserService userService,  JoinWordRepository joinWordRepository) {
        this.userService = userService;
        this.wordService = wordService;
        this.joinWordRepository = joinWordRepository;
    }

    public boolean checkWordUserSet(Long userId, String wordInfo) {
        Word foundWord = this.wordService.getWordByWordInfo(wordInfo);
        if(foundWord == null) return false;
        JoinWordUser checkExist = this.joinWordRepository.findByUserWithWord(userId, foundWord.getUid());
        return checkExist != null;
    }

    /**
     * 유저가 입력한 단어 생성
     * @param userId 유저 아이디
     * @param wordInfo 단어 ex> name <= 이름
     */
    @Transactional
    public JoinWordUser generate(Long userId, String wordInfo, String wordMeaning, WordEditStatus editStatus) {
        User userInfo = this.userService.findById(userId);
        // 이미 단어가 존재하는 지 확인 한다.
        Word foundWord = this.wordService.getWordByWordInfo(wordInfo);

        // 단어가 없는 경우 저장을 한다.
        if (foundWord == null) {
            foundWord = this.wordService.saveWord(wordInfo, wordMeaning, editStatus);
        }

        String jwuId = this.joinWordRepository.saveUserWord(userInfo, foundWord);
        return this.joinWordRepository.findById(jwuId);
    }

    // 유저가 입력간 단어 목록 갖고 오기
    public WordListForm[] getUserWordList(Long userId) {
        JoinWordUser[] joinWordUsers = this.joinWordRepository.userOwnedWordList(userId);
        //
        String[] ids = new String[joinWordUsers.length];

        for (int idx = 0; idx < joinWordUsers.length; idx++) {
            ids[idx] = joinWordUsers[idx].getWord().getUid();
        }
        Word[] wordByIds = this.wordService.getWordByIds(ids);

        WordListForm[] output = new WordListForm[wordByIds.length];

        for (int index = 0; index < wordByIds.length ; index++) {
            WordListForm wordListForm = new WordListForm();
            wordListForm.setWordId(wordByIds[index].getUid());
            wordListForm.setWord(wordByIds[index].getWord());
            wordListForm.setMeaning(wordByIds[index].getMeaning());
            wordListForm.setStatus(wordByIds[index].getStatus());
            wordListForm.setUserId(userId);
            output[index] = wordListForm;
        }
        return output;
    }

    /*
     * 최근 10개 단어 뽑아오기
     * TODO: 7만 최근 단어 3개는 랜덤으로 추출
     */
    public JoinWordUser[] pickWordsForQuiz(Long userId) {
        return this.joinWordRepository.pickListForQuiz(userId);
    }

//    public JoinWordUser getWordUserWithWord(JoinWordUser joinWordUser) {
//
//    }

}
