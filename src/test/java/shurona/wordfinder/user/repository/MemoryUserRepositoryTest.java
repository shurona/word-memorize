package shurona.wordfinder.user.repository;

import org.junit.jupiter.api.Test;
import shurona.wordfinder.user.User;
import shurona.wordfinder.user.repository.MemoryUserRepository;
import shurona.wordfinder.user.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

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