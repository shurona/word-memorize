package shurona.wordfinder.word.repository.joinuserword;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import shurona.wordfinder.word.domain.JoinWordUser;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DatabaseJoinWordRepositoryTest {

    @Autowired
    private DatabaseJoinWordRepository joinWordRepository;

    @Test
    public void 저장_조회() {
        // given
        Long userId = 1L;
        String wordId = UUID.randomUUID().toString();

        // when
        JoinWordUser joinWordUser = this.joinWordRepository.saveUserWord(userId, wordId);
        JoinWordUser joinById = this.joinWordRepository.findById(joinWordUser.getId());


        // then
        assertThat(joinWordUser.getWordId()).isEqualTo(wordId);
        assertThat(joinWordUser.getUserId()).isEqualTo(userId);
        assertThat(joinWordUser).isEqualTo(joinById);
    }

    @Test
    public void 유저보유_목록() {
        // given
        long userId = 1L;
        String wordId = UUID.randomUUID().toString();
        for (int i = 0; i < 30; i++) {
            wordId = UUID.randomUUID().toString();
            if (i >= 20) {
                userId = 2L;
            }
            // TODO: Bulk 저장 되나
            this.joinWordRepository.saveUserWord(userId, wordId);
        }
        // when
        JoinWordUser[] joinWordUsers = this.joinWordRepository.userOwnedWordList(1L);

        // then
        assertThat(joinWordUsers.length).isEqualTo(20);
    }

    @Test
    public void 전체_목록_조회() {
        // given
        for (long i = 0L; i < 10; i++) {
            this.joinWordRepository.saveUserWord(i, UUID.randomUUID().toString());
        }

        // when
        JoinWordUser[] joinWordUsers = this.joinWordRepository.joinWordList();

        // then
        assertThat(joinWordUsers.length).isEqualTo(10);
    }

}