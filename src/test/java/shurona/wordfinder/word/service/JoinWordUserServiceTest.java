package shurona.wordfinder.word.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shurona.wordfinder.user.domain.User;
import shurona.wordfinder.user.repository.MemoryUserRepository;
import shurona.wordfinder.user.repository.UserRepository;
import shurona.wordfinder.user.service.UserService;
import shurona.wordfinder.word.domain.JoinWordUser;
import shurona.wordfinder.word.domain.Word;
import shurona.wordfinder.word.dto.WordListForm;
import shurona.wordfinder.word.repository.joinuserword.JoinWordRepository;
import shurona.wordfinder.word.repository.joinuserword.MemoryJoinWordRepository;
import shurona.wordfinder.word.repository.word.MemoryWordRepository;
import shurona.wordfinder.word.repository.word.WordRepository;

import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JoinWordUserServiceTest {
    private JoinWordUserService joinWordUserService;
    private WordRepository wordRepository;
    private JoinWordRepository joinWordRepository;
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    public void beforeEach() {
        this.wordRepository = new MemoryWordRepository();
        this.userRepository = new MemoryUserRepository();
        this.joinWordRepository = new MemoryJoinWordRepository();
        this.joinWordUserService = new JoinWordUserService(
                new WordService(
                        this.wordRepository
                ),
                new UserService(
                        this.userRepository
                ),
                this.joinWordRepository
        );
    }

    @Test
    void generateTest() {
        // given
        User user = this.userRepository.save(new User("nick", "log", "passwd"));
        String wordInfo = "HelloWorld";
        String wordMeaning = "안녕";

        // when
        JoinWordUser userWithWord = this.joinWordUserService.generate(user.getId(), wordInfo, wordMeaning);

        // then
        assertThat(userWithWord.getUser().getId()).isEqualTo(user.getId());
        assertThat(this.wordRepository.findWordByWord(wordInfo).getUid()).isEqualTo(userWithWord.getWord().getUid());
    }

    @Test
    void getUserWordList() {
        // given
        String wordUid = UUID.randomUUID().toString();
        Word exWord = new Word("Hello", "안녕");
        exWord.setUid(wordUid);
        Word savedWord = this.wordRepository.save(exWord);

        User userOne = this.userRepository.save(new User("nickname1", "loginId1", "password1"));
        User userTwo = this.userRepository.save(new User("nickname2", "loginId2", "password2"));
        User userThree = this.userRepository.save(new User("nickname3", "loginId3", "password3"));
        User[] userList = {userOne, userTwo, userThree};

        ArrayList<JoinWordUser> userWithWordList = new ArrayList<>();
        long wishUserId = userOne.getId();
        for (int i = 0; i < 100; i++) {
            JoinWordUser output = this.joinWordRepository.saveUserWord(userList[i % 3], savedWord);
            if (i % 3 == wishUserId - 1) {
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
}