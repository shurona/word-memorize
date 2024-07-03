package shurona.wordfinder.word.repository.joinuserword;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import shurona.wordfinder.user.domain.User;
import shurona.wordfinder.user.repository.DatabaseUserRepository;
import shurona.wordfinder.word.domain.JoinWordUser;
import shurona.wordfinder.word.domain.Word;
import shurona.wordfinder.word.repository.word.DatabaseWordRepository;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;


@Sql(scripts = {"/resetTable.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DatabaseJoinWordRepositoryTest {

    @Autowired
    private DatabaseJoinWordRepository joinWordRepository;
    @Autowired
    private DatabaseUserRepository userRepository;
    @Autowired
    private DatabaseWordRepository wordRepository;

    @Test
    public void 저장_조회() {
        // given
        Long userId = this.userRepository.save(new User("nickname", "loginId", "password"));
        User user = this.userRepository.findById(userId);

        String wordId = this.wordRepository.save(new Word("wd", "meaning"));
        Word word = this.wordRepository.findWordById(wordId).get();

        // when
        String joinWordUserId = this.joinWordRepository.saveUserWord(user, word);
        JoinWordUser joinWordUser = this.joinWordRepository.findById(joinWordUserId);

        JoinWordUser joinById = this.joinWordRepository.findById(joinWordUser.getId());


        // then
        assertThat(joinWordUser.getWord().getUid()).isEqualTo(word.getUid());
        assertThat(joinWordUser.getUser().getId()).isEqualTo(user.getId());
        assertThat(joinWordUser).isEqualTo(joinById);
    }

    @Test
    public void 유저보유_목록() {
        // given
        Long userOneId = this.userRepository.save(new User("nicknameOne", "loginId", "password"));
        Long userTwoId = this.userRepository.save(new User("nicknameTwo", "loginId", "password"));

        User userOne = this.userRepository.findById(userOneId);
        User userTwo = this.userRepository.findById(userTwoId);

        String wordString = "wd";
        String wordMeaning = "meaning";

        for (int i = 0; i < 30; i++) {
            // TODO: Bulk 저장 되나
            String wordId = this.wordRepository.save(new Word(wordString + i, wordMeaning + i));
            Word word = this.wordRepository.findWordById(wordId).get();
            if (i >= 20) {
                this.joinWordRepository.saveUserWord(userTwo, word);
            } else {
                this.joinWordRepository.saveUserWord(userOne, word);
            }
        }

        // when
        JoinWordUser[] joinWordUsers = this.joinWordRepository.userOwnedWordList(userOne.getId());

        // then
        assertThat(joinWordUsers.length).isEqualTo(20);
    }

    @Test
    public void 전체_목록_조회() {
        // given
        Word[] wordList = new Word[10];
        User[] userList = new User[10];
        for (int i = 0; i < 10; i++) {
            String wordId = this.wordRepository.save(new Word("word " + i, "mn" + i));
            wordList[i] = this.wordRepository.findWordById(wordId).get();

            Long userId = this.userRepository.save(new User("nicknameTwo", "loginId", "password"));
            userList[i] = this.userRepository.findById(userId);
        }


        for (int i = 0; i < 10; i++) {
            this.joinWordRepository.saveUserWord(userList[i], wordList[i]);

        }

        // when
        JoinWordUser[] joinWordUsers = this.joinWordRepository.joinWordList();

        // then
        assertThat(joinWordUsers.length).isEqualTo(10);
    }

    @Test
    public void 단어_유저_검색() {
        // given
        Long userInfoId = this.userRepository.save(new User("nicknameOne", "loginId", "password"));
        User userInfo = this.userRepository.findById(userInfoId);

        String wordId = this.wordRepository.save(new Word("wd", "meaning"));
        String notSavedId = this.wordRepository.save(new Word("nonWd", "noMean"));
        Word wordInfo = this.wordRepository.findWordById(wordId).get();
        Word notSavedWord = this.wordRepository.findWordById(notSavedId).get();
        this.joinWordRepository.saveUserWord(userInfo, wordInfo);

        // when
        JoinWordUser output = this.joinWordRepository.findByUserWithWord(userInfo.getId(), wordInfo.getUid());
        JoinWordUser no = this.joinWordRepository.findByUserWithWord(userInfo.getId(), notSavedWord.getUid());

        // then
        assertThat(output.getWord().getWord()).isEqualTo(wordInfo.getWord());
        assertThat(no).isEqualTo(null);
    }

    @Test
    public void 퀴즈일부_랜덤겟_확인() {
        // given
        Long userOneId = this.userRepository.save(new User("nicknameOne", "loginId", "password"));

        User userOne = this.userRepository.findById(userOneId);

        String wordString = "wd";
        String wordMeaning = "meaning";

        for (int i = 0; i < 15; i++) {
            String wordId = this.wordRepository.save(new Word(wordString + i, wordMeaning + i));
            Word word = this.wordRepository.findWordById(wordId).get();
            this.joinWordRepository.saveUserWord(userOne, word);
        }

        // when
        JoinWordUser[] except = this.joinWordRepository.pickListForQuiz(userOneId, 0, 7);
        JoinWordUser[] needList = this.joinWordRepository.pickRandomForQuiz(userOneId, 7, 3);
//        ArrayList<JoinWordUser> convertList = new ArrayList<>(List.of(needList));

        // then
        int i = 0;
        for (JoinWordUser nd : needList) {
            for (JoinWordUser ex : except) {
                assertThat(nd.getId()).isNotEqualTo(ex.getId());
            }
        }
    }
}