package shurona.wordfinder.word.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shurona.wordfinder.user.domain.User;
import shurona.wordfinder.user.repository.UserRepository;
import shurona.wordfinder.user.service.UserService;
import shurona.wordfinder.word.domain.JoinWordUser;
import shurona.wordfinder.word.domain.Word;
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

    /**
     * 유저가 입력한 단어 생성
     * @param userId 유저 아이디
     * @param wordInfo 단어 ex> name <= 이름
     */
    @Transactional
    public JoinWordUser generate(Long userId, String wordInfo, String wordMeaning) {
        User userInfo = this.userService.findById(userId);
        // 이미 단어가 존재하는 지 확인 한다.
        Word foundWord = this.wordService.getWordByWordInfo(wordInfo);

        // 단어가 없는 경우 저장을 한다.
        if (foundWord == null) {
            foundWord = this.wordService.saveWord(wordInfo, wordMeaning);
        }

        // 이미 단어 쌍이 존재하는 지 확인한다.
        JoinWordUser checkExist = this.joinWordRepository.findByUserWithWord(userInfo, foundWord);

        if (checkExist != null) {
            return null;
        }

        return this.joinWordRepository.saveUserWord(userInfo, foundWord);
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
            wordListForm.setUserId(userId);
            output[index] = wordListForm;
        }
        return output;
    }

    /**
     * User가 갖고 있는 단어 목록을 갖고 온다.
     */
    public WordListForm[] getUserWordList() {
        JoinWordUser[] joinWordUsers = this.joinWordRepository.joinWordList();

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
