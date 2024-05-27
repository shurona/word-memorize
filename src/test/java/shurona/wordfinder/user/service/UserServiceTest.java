package shurona.wordfinder.user.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shurona.wordfinder.user.User;
import shurona.wordfinder.user.service.UserService;
import shurona.wordfinder.user.repository.MemoryUserRepository;
import shurona.wordfinder.user.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceTest {

    UserService userService;
    MemoryUserRepository userRepository;

    @BeforeEach
    public void beforeEach() {
        this.userRepository = new MemoryUserRepository();
        this.userService = new UserService(this.userRepository);
    }


    @Test
    void join() {

        //given, when
        Long userId1 = this.userService.join("nickNameOne", "loginId1", "pwd1");
        Long userId2 = this.userService.join("nickNameTwo", "loginId2", "pwd2");

        // then
        // 증가 확인
        assertThat(userId1).isEqualTo(userId2 - 1);
    }

    @Test
    void findById() {
        Long userId1 = this.userService.join("userOne", "loginId1", "pwd1");
        Long userId2 = this.userService.join("userTwo", "loginId2", "pwd2");
        assertThat(this.userService.findById(userId1).getNickname()).isEqualTo("userOne");
        assertThat(this.userService.findById(userId2).getNickname()).isNotEqualTo("userOne");
    }

    @Test
    void 로그인_테스트() {
        String loginId = "login";
        String password = "pwd";
        //given
        this.userService.join("nickname", loginId, password);

        //when
        User loginUser = this.userService.login(loginId, password);

        //then
        assertThat(loginUser).isNotNull();
    }

    @Test
    void 비_로그인_테스트() {
        String loginId = "login";
        String password = "pwd";

        String wrongId = "hello";
        String wrongPwd = "pdd";
        //given
        this.userService.join("nickname", loginId, password);

        //when
        User loginUserOne = this.userService.login(wrongId, password);
        User loginUserTwo = this.userService.login(loginId, wrongPwd);
        User loginUserThree = this.userService.login(wrongId, wrongPwd);

        //then
        assertThat(loginUserOne).isNull();
        assertThat(loginUserTwo).isNull();
        assertThat(loginUserThree).isNull();
    }

    @Test
    void 유저닉네임_중복서비스_테스트() {
        String loginId = "login";
        String password = "pwd";

        //given
        this.userService.join("nickname", loginId, password);
        this.userService.join("ssss", "loginId2", password);

        assertThat(this.userService.checkUserNicknameDup("nickname")).isEqualTo(true);
        assertThat(this.userService.checkUserNicknameDup("ddd")).isEqualTo(false);
    }

    @Test
    void 유저로그인아이디_중복서비스_테스트() {
        String loginId = "login";
        String password = "pwd";

        //given
        this.userService.join("nickname", loginId, password);

        assertThat(this.userService.checkUserLoginIdDup("login")).isEqualTo(true);
        assertThat(this.userService.checkUserLoginIdDup("ddd")).isEqualTo(false);
    }
}