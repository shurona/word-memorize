package shurona.wordfinder.word.repository.joinuserword;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shurona.wordfinder.user.domain.User;
import shurona.wordfinder.user.repository.MemoryUserRepository;
import shurona.wordfinder.word.domain.JoinWordUser;
import shurona.wordfinder.word.domain.Word;
import shurona.wordfinder.word.repository.word.MemoryWordRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class MemoryJoinWordRepositoryTest {

    MemoryJoinWordRepository joinWordRepository;
    MemoryUserRepository userRepository;
    MemoryWordRepository wordRepository;

    @BeforeEach
    public void beforeEach() {
        this.joinWordRepository = new MemoryJoinWordRepository();
        this.userRepository = new MemoryUserRepository();
        this.wordRepository = new MemoryWordRepository();
        this.userRepository.clearStore();
        this.wordRepository.clearStore();
        this.joinWordRepository.clearStore();
    }

    // 유저의 단어 저장
    @Test
    void saveUserWord() {
        //given
        Long userInfoId = this.userRepository.save(new User("", "", ""));
        User userInfo = this.userRepository.findById(userInfoId);

        String wordId = this.wordRepository.save(new Word("word", "meaning"));
        Word wordInfo = this.wordRepository.findWordById(wordId).get();

        //when
        String oneRelationId = this.joinWordRepository.saveUserWord(userInfo, wordInfo);
        JoinWordUser oneRelation = this.joinWordRepository.findById(oneRelationId);

        //then
        assertThat(userInfo.getId()).isEqualTo(oneRelation.getUser().getId());
        assertThat(wordInfo.getUid()).isEqualTo(oneRelation.getWord().getUid());
    }

    // 유저가 보유한 단어 목록 보여준다.
    @Test
    void userOwnedWordList() {
        //given
        ArrayList<JoinWordUser> userWithWordList = new ArrayList<>();

        Long userOneId = this.userRepository.save(new User("nickname1", "loginId1", "password1"));
        Long userTwoId = this.userRepository.save(new User("nickname2", "loginId2", "password2"));
        Long userThreeId = this.userRepository.save(new User("nickname3", "loginId3", "password3"));

        User userOne = this.userRepository.findById(userOneId);
        User userTwo = this.userRepository.findById(userTwoId);
        User userThree = this.userRepository.findById(userThreeId);

        User[] userList = {userOne, userTwo, userThree};

        String wordId = this.wordRepository.save(new Word("", ""));
        Word wordInfo = this.wordRepository.findWordById(wordId).get();

        long wishUserId = userOne.getId();
        for (int i = 0; i < 100; i++) {
            String outputId = this.joinWordRepository.saveUserWord(userList[i % 3], wordInfo);
            JoinWordUser output = this.joinWordRepository.findById(outputId);
            if (i % 3 == wishUserId - 1) {
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
            assertThat(joinWordUser.getUser().getId()).isEqualTo(wishUserId);
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