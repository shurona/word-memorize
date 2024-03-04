package shurona.wordfinder.service;

import shurona.wordfinder.domain.User;
import shurona.wordfinder.repository.MemoryUserRepository;
import shurona.wordfinder.repository.UserRepository;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository memoryUserRepository) {
        this.userRepository = memoryUserRepository;
    }


    public Long join(String nickName) {

        User user = new User();
        user.setNickname(nickName);
        user = userRepository.save(user);

        return user.getId();
    }
}
