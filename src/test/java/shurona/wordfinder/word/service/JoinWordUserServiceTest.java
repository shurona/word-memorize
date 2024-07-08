package shurona.wordfinder.word.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import shurona.wordfinder.user.domain.User;
import shurona.wordfinder.user.repository.UserRepository;
import shurona.wordfinder.user.service.UserService;
import shurona.wordfinder.word.domain.JoinWordUser;
import shurona.wordfinder.word.domain.Word;
import shurona.wordfinder.word.domain.WordEditStatus;
import shurona.wordfinder.word.dto.WordListForm;
import shurona.wordfinder.word.repository.joinuserword.JoinWordRepository;
import shurona.wordfinder.word.repository.word.WordRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

//@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@Sql(scripts = {"/resetTable.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class JoinWordUserServiceTest {
    @Autowired
    private JoinWordUserService joinWordUserService;
    @Autowired
    private WordRepository wordRepository;
    @Autowired
    private JoinWordRepository joinWordRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Test
    void generateTest() {
        // given
        Long userId = this.userRepository.save(new User("nick", "log", "passwd"));
        User user = this.userRepository.findById(userId);
        String wordInfo = "HelloWorld";
        String wordMeaning = "안녕";

        // when
        JoinWordUser userWithWord = this.joinWordUserService.generate(user.getId(), wordInfo, wordMeaning, WordEditStatus.COMPLETE);

        // then
        assertThat(userWithWord.getUser().getId()).isEqualTo(user.getId());
        assertThat(this.wordRepository.findWordByWord(wordInfo).getUid()).isEqualTo(userWithWord.getWord().getUid());
    }

    @Test
    void checkExist() {
        // given
        Long userId = this.userRepository.save(new User("nick2", "log", "passwd"));
        User user = this.userRepository.findById(userId);
        String savedWordId = this.wordRepository.save(new Word("Hello", "안녕"));
        Word savedWord = this.wordRepository.findWordById(savedWordId).get();
        this.joinWordRepository.saveUserWord(user, savedWord);

        // when
        boolean exist = this.joinWordUserService.checkWordUserSet(userId, savedWord.getWord());
        boolean nonExist = this.joinWordUserService.checkWordUserSet(2L, savedWord.getWord());

        // then
        assertThat(exist).isTrue();
        assertThat(nonExist).isFalse();
    }

    @Test
    void getUserWordList() {
        // given
        User firstUser = new User("nickname1", "loginId1", "password1");
        Long userOneId = this.userRepository.save(firstUser);
        Long userTwoId = this.userRepository.save(new User("nickname2", "loginId2", "password2"));
        Long userThreeId = this.userRepository.save(new User("nickname3", "loginId3", "password3"));

        User userOne = this.userRepository.findById(userOneId);
        User userTwo = this.userRepository.findById(userTwoId);
        User userThree = this.userRepository.findById(userThreeId);

        User[] userList = {userOne, userTwo, userThree};

        ArrayList<JoinWordUser> userWithWordList = new ArrayList<>();
        long wishUserId = userOne.getId();
        for (int i = 0; i < 100; i++) {
            String savedWordId = this.wordRepository.save(new Word("Hello " + i, "안녕"));
            Word savedWord = this.wordRepository.findWordById(savedWordId).get();
            String outputId = this.joinWordRepository.saveUserWord(userList[i % 3], savedWord);
            JoinWordUser output = this.joinWordRepository.findById(outputId);
            if (i % 3 == wishUserId - userOneId) {
                userWithWordList.add(output);
            }
        }

        // when
        WordListForm[] userWordList = this.joinWordUserService.getUserWordList(wishUserId);

        // then
        assertThat(userWithWordList.size()).isEqualTo(userWordList.length);
        for (WordListForm joinWordUser : userWordList) {
            assertThat(joinWordUser.getUserId()).isEqualTo(wishUserId);
        }
    }

    @Test
    public void 단어숨기기_확인() {
        // given
        User firstUser = new User("nickname", "loginId", "password1");
        Long userOneId = this.userRepository.save(firstUser);
        User userOne = this.userRepository.findById(userOneId);

        List<String> jwuIds = new ArrayList<>();
        // 단어 100개 저장
        for (int i = 0; i < 20; i++) {
            String savedWordId = this.wordRepository.save(new Word("Hello " + i, "안녕"));
            Word savedWord = this.wordRepository.findWordById(savedWordId).get();
            String outputId = this.joinWordRepository.saveUserWord(userOne, savedWord);
            jwuIds.add(outputId);
        }

        // when
        JoinWordUser jwuOne = this.joinWordRepository.findById(jwuIds.get(0));
        jwuOne.hideWordVisible();
        JoinWordUser[] jwuList = this.joinWordRepository.userOwnedWordList(userOneId);

        // then
        assertThat(jwuList.length).isEqualTo(19);
    }
}