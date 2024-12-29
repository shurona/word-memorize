package shurona.wordfinder.user.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import shurona.wordfinder.user.domain.User;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DatabaseUserRepositoryTest {

    @Autowired
    private DatabaseUserRepository userRepository;

    @Test
    public void 회원가입_조회() {
        //given
        String nickName = "nicknameOne";
        User user = new User(nickName, "test", "password");

        //when
        Long saveId = this.userRepository.save(user);
        User save = this.userRepository.findById(saveId);

        //then
        assertThat(nickName).isEqualTo(save.getNickname());

    }

    @Test
    public void 전체_조회() {
        // given
        String nickName = "nicknameTwo";
        int wishCount = 10;
        for (int i = 0; i < wishCount; i++) {
            User user = new User(nickName + " " + i, "test" + " " + i, "password" + " " + i);
            this.userRepository.save(user);
        }

        // when
        Long[] userIds = this.userRepository.userIds();

        // then
        assertThat(userIds.length).isEqualTo(wishCount);

    }

    @Test
    public void 닉네임_조회() {

        //given
        String nickName = "nicknameTwo";
        User user = new User(nickName, "test", "password");

        //when
        this.userRepository.save(user);
        Optional<User> byNickname = this.userRepository.findByNickname(nickName);
        User user1 = byNickname.orElse(null);

        //then
        if (user1 == null) {
            assertThat(user1).isEqualTo(null);
        } else {
            assertThat(user1.getNickname()).isEqualTo(nickName);
        }
    }

    @Test
    public void 로그인_테스트() {
        //given
        String nickName = "nicknameThree";
        String loginId = "loginTest";
        String password = "passwd";
        User user = new User(nickName, loginId, password);

        this.userRepository.save(user);

        //when
        User checkLoginUser = this.userRepository.login(loginId, password).orElse(null);
        User unknownUser = this.userRepository.login("udkdkd", password).orElse(null);

        //then
        if (checkLoginUser != null) {
            assertThat(checkLoginUser.getNickname()).isEqualTo(nickName);
        }
        assertThat(unknownUser).isEqualTo(null);
    }


}