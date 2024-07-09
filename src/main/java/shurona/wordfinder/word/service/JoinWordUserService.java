package shurona.wordfinder.word.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class JoinWordUserService {

    private final Logger log = LoggerFactory.getLogger(getClass());
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
     * 유저가 해당 단어를 입력했는지 확인한다.
     */
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

    /**
     * 숨긴 단어를 제외한 유저가 저장한 단어 갯수
     */
    public int checkUserWordCount(Long userId, boolean excludeHide) {
        return this.joinWordRepository.countWordUserByUserId(userId, excludeHide);
    }

    /**
     * 유저가 입력간 단어 목록 갖고 오기
     */
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

    /**
     * quiz를 위한 단어들을 선택한다.
     * 최근 7개  3개는 랜덤
     */
    public JoinWordUser[] pickWordsForQuiz(Long userId) {
        int recentLimit = 7;
        int notRecentSize = 3;
        // 최근 7개 단어를 불러온다.
        JoinWordUser[] selectedRecentList = this.joinWordRepository.pickListForQuiz(userId, 0, recentLimit);

        JoinWordUser[] selectRandom = this.joinWordRepository.pickRandomForQuiz(userId, recentLimit, notRecentSize);

        // 결과 리스트 초기화
        List<JoinWordUser> combineQuiz = new ArrayList<>(Arrays.asList(selectedRecentList));
        combineQuiz.addAll(Arrays.asList(selectRandom).subList(0, 3));
        return combineQuiz.toArray(JoinWordUser[]::new);
    }

    /**
     * 유저가 입력한 단어를 숨기기
     */
    @Transactional
    public void hideWordsByUser(Long userId, String wordUid) {
        JoinWordUser jwuInfo = this.joinWordRepository.findByUserWithWord(userId, wordUid);
        // get joinWordUser by ids
        // 확장성을 위해 ids 목록으로 검색
        List<String> joinWordUserIds = new ArrayList<>();
//        for (JoinWordUser joinWordUser : joinWordUserList) {
//            joinWordUserIds.add(joinWordUser.getId());
//        }
        joinWordUserIds.add(jwuInfo.getId());
        List<JoinWordUser> jwuList = this.joinWordRepository.findListByIds(joinWordUserIds);
        // 단어를 숨김 처리 후 count 증가
        for (JoinWordUser joinWordUser : jwuList) {
            joinWordUser.hideWordVisible();
            joinWordUser.getWord().increaseHideCount();
        }
    }

}
