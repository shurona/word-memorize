package shurona.wordfinder.word.repository.joinuserword;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import shurona.wordfinder.user.domain.User;
import shurona.wordfinder.user.repository.DatabaseUserRepository;
import shurona.wordfinder.word.domain.JoinWordUser;
import shurona.wordfinder.word.domain.Word;
import shurona.wordfinder.word.repository.word.DatabaseWordRepository;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


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
        User user = this.userRepository.save(new User("nickname", "loginId", "password"));
        Word word = this.wordRepository.save(new Word("wd", "meaning"));

        // when
        JoinWordUser joinWordUser = this.joinWordRepository.saveUserWord(user, word);
        JoinWordUser joinById = this.joinWordRepository.findById(joinWordUser.getId());


        // then
        assertThat(joinWordUser.getWord().getUid()).isEqualTo(word.getUid());
        assertThat(joinWordUser.getUser().getId()).isEqualTo(user.getId());
        assertThat(joinWordUser).isEqualTo(joinById);
    }

    @Test
    public void 유저보유_목록() {
        // given
        User userOne = this.userRepository.save(new User("nicknameOne", "loginId", "password"));
        User userTwo = this.userRepository.save(new User("nicknameTwo", "loginId", "password"));
        Word word = this.wordRepository.save(new Word("wd", "meaning"));
        for (int i = 0; i < 30; i++) {
            // TODO: Bulk 저장 되나
            if (i >= 20) {
                this.joinWordRepository.saveUserWord(userTwo, word);
            } else {
                this.joinWordRepository.saveUserWord(userOne, word);
            }
        }

        System.out.println("유저 아이디 : " + userOne.getId());
        // when
        JoinWordUser[] joinWordUsers = this.joinWordRepository.userOwnedWordList(userOne.getId());

        System.out.print("정보를 달라 : ");
        System.out.println(joinWordUsers[0].getWord());

        // then
        assertThat(joinWordUsers.length).isEqualTo(20);
    }

    @Test
    public void 전체_목록_조회() {
        // given
        Word[] wordList = new Word[10];
        User[] userList = new User[10];
        for (int i = 0; i < 10; i++) {
            wordList[i] = this.wordRepository.save(new Word("word " + i, "mn" + i));
            userList[i] = this.userRepository.save(new User("nicknameTwo", "loginId", "password"));
        }


        for (int i = 0; i < 10; i++) {
            this.joinWordRepository.saveUserWord(userList[i], wordList[i]);

        }

        // when
        JoinWordUser[] joinWordUsers = this.joinWordRepository.joinWordList();

        // then
        assertThat(joinWordUsers.length).isEqualTo(10);
    }

}