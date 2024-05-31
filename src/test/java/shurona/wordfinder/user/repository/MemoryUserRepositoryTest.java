package shurona.wordfinder.user.repository;

import org.junit.jupiter.api.Test;
import shurona.wordfinder.user.domain.User;

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

    @Test
    void 유저목록만_갖고_오기() {
        //given
        User userOne = new User();
        userOne.setNickname("hello");

        User userTwo = new User();
        userTwo.setNickname("hi");

        userRepository.save(userOne);
        userRepository.save(userTwo);


        //when
        Long[] longs = this.userRepository.userIds();

        //then
        assertThat(longs.length).isEqualTo(2);

    }
}