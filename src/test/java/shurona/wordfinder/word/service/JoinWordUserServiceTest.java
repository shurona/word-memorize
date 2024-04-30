package shurona.wordfinder.word.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shurona.wordfinder.word.JoinWordUser;
import shurona.wordfinder.word.repository.joinuserword.JoinWordRepository;
import shurona.wordfinder.word.repository.joinuserword.MemoryJoinWordRepository;
import shurona.wordfinder.word.repository.word.MemoryWordRepository;
import shurona.wordfinder.word.repository.word.WordRepository;

import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JoinWordUserServiceTest {
    private JoinWordUserService joinWordUserService;
    private WordRepository wordRepository;
    private JoinWordRepository joinWordRepository;

    @BeforeEach
    public void beforeEach() {
        this.wordRepository = new MemoryWordRepository();
        this.joinWordRepository = new MemoryJoinWordRepository();
        this.joinWordUserService = new JoinWordUserService(
                new WordService(
                        this.wordRepository
                ),
                this.joinWordRepository
        );
    }

    @Test
    void generateTest() {
        // given
        Long userId = 13L;
        String wordInfo = "HelloWorld";
        String wordMeaning = "안녕";

        // when
        JoinWordUser userWithWord = this.joinWordUserService.generate(userId, wordInfo, wordMeaning);

        // then
        assertThat(userWithWord.getUserId()).isEqualTo(userId);
        assertThat(this.wordRepository.findWordByWord(wordInfo).getUid()).isEqualTo(userWithWord.getWordId());
    }

    @Test
    void getUserWordList() {
        // given
        ArrayList<JoinWordUser> userWithWordList = new ArrayList<>();
        long wishUserId = 1L;
        for (int i = 0; i < 100; i++) {
            JoinWordUser output = this.joinWordRepository.saveUserWord((long) (i % 3), UUID.randomUUID());
            if (i % 3 == wishUserId) {
                userWithWordList.add(output);
            }
        }

        // when
        JoinWordUser[] userWordList = this.joinWordUserService.getUserWordList(wishUserId);

        // then
        assertThat(userWithWordList.size()).isEqualTo(userWordList.length);
        for (JoinWordUser joinWordUser : userWordList) {
            assertThat(joinWordUser.getUserId()).isEqualTo(wishUserId);
        }
    }
}