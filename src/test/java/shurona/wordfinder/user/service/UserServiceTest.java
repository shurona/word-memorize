package shurona.wordfinder.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shurona.wordfinder.user.service.UserService;
import shurona.wordfinder.user.repository.MemoryUserRepository;
import shurona.wordfinder.user.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceTest {

    UserService userService;
    UserRepository userRepository;

    @BeforeEach
    public void beforeEach() {
        this.userRepository = new MemoryUserRepository();
        this.userService = new UserService(this.userRepository);
    }

    @Test
    void join() {

        //given, when
        Long userId1 = this.userService.join("nickNameOne");
        Long userId2 = this.userService.join("nickNameTwo");

        // then
        // 증가 확인
        assertThat(userId1).isEqualTo(userId2 - 1);
    }

    @Test
    void findById() {
        Long userId1 = this.userService.join("userOne");
        Long userId2 = this.userService.join("userTwo");
        assertThat(this.userService.findById(userId1).getNickname()).isEqualTo("userOne");
        assertThat(this.userService.findById(userId2).getNickname()).isNotEqualTo("userOne");
    }
}