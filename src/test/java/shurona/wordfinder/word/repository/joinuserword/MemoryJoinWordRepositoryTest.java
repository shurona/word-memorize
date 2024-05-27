package shurona.wordfinder.word.repository.joinuserword;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shurona.wordfinder.word.JoinWordUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class MemoryJoinWordRepositoryTest {

    MemoryJoinWordRepository joinWordRepository;

    @BeforeEach
    public void beforeEach() {
        this.joinWordRepository = new MemoryJoinWordRepository();
        this.joinWordRepository.clearStore();
    }

    // 유저의 단어 저장
    @Test
    void saveUserWord() {
        //given
        long userId = 1L;
        String wordId = UUID.randomUUID().toString();

        //when
        JoinWordUser oneRelation = this.joinWordRepository.saveUserWord(userId, wordId);

        //then
        assertThat(userId).isEqualTo(oneRelation.getUserId());
        assertThat(wordId).isEqualTo(oneRelation.getWordId());
    }

    // 유저가 보유한 단어 목록 보여준다.
    @Test
    void userOwnedWordList() {
        //given
        ArrayList<JoinWordUser> userWithWordList = new ArrayList<>();
        long wishUserId = 1L;
        for (int i = 0; i < 100; i++) {
            JoinWordUser output = this.joinWordRepository.saveUserWord((long) (i % 3), UUID.randomUUID().toString());
            if (i % 3 == wishUserId) {
                userWithWordList.add(output);
            }
        }

        //when
        JoinWordUser[] wordListByUser = this.joinWordRepository.userOwnedWordList(wishUserId);

        //then
        // 저장된 길이와 쿼리가 같은지 확인한다.
        assertThat(userWithWordList.size()).isEqualTo(wordListByUser.length);
        for (JoinWordUser joinWordUser : wordListByUser) {
            // 원하는 유저인지 확인한다.
            assertThat(joinWordUser.getUserId()).isEqualTo(wishUserId);
        }
    }

    @Test
    void testUnsavedWords() {
        // given
        JoinWordUser[] joinWordUsers = this.joinWordRepository.userOwnedWordList(1L);
        // when

        System.out.println("joinWordUsers = " + Arrays.toString(joinWordUsers));
        // then
        assertThat(joinWordUsers).isEmpty();

    }
}