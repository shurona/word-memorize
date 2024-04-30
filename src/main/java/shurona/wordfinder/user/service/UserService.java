package shurona.wordfinder.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shurona.wordfinder.user.User;
import shurona.wordfinder.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository memoryUserRepository) {
        this.userRepository = memoryUserRepository;
    }


    public Long join(String nickName) {

        User user = new User();
        user.setNickname(nickName);
        user = userRepository.save(user);

        return user.getId();
    }

    public User findById(Long id) {

        return this.userRepository.findById(id);
    }

    public List<Integer> findUserIds() {
        Long[] longs = this.userRepository.userIds();

        List<Integer> output = new ArrayList<>();
        for (Long al : longs) {
            output.add(Math.toIntExact(al));
        }
        return output;
    }
}
