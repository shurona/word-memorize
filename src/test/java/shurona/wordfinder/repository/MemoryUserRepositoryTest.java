package shurona.wordfinder.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shurona.wordfinder.domain.User;
import shurona.wordfinder.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MemoryUserRepositoryTest {

    UserRepository userRepository = new MemoryUserRepository();


    @Test
    void save() {

        User userOne = new User();
        User one = userRepository.save(userOne);

        User userTwo = new User();
        User two = userRepository.save(userTwo);

        assertThat(one.getId()).isEqualTo(two.getId() - 1);
    }

    @Test
    void findById() {
        User userOne = new User();
        userOne.setNickname("hello");
        User one = userRepository.save(userOne);

        assertThat(one).isEqualTo(userRepository.findById(one.getId()));
    }

    @Test
    void findByNickname() {
        User userOne = new User();
        userOne.setNickname("hello");
        User one = userRepository.save(userOne);

        assertThat(one).isEqualTo(userRepository.findByNickname(one.getNickname()).get());
    }
}