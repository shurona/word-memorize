package shurona.wordfinder.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shurona.wordfinder.user.domain.User;
import shurona.wordfinder.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String loginId, String password) {
        //
        Optional<User> login = this.userRepository.login(loginId, password);

        return login.orElse(null);
    }

    public boolean checkUserLoginIdDup(String loginId) {
        Optional<User> userInfo = this.userRepository.findByLoginId(loginId);
        return userInfo.orElse(null) != null;
    }

    public boolean checkUserNicknameDup(String nickname) {
        Optional<User> userInfo = this.userRepository.findByNickname(nickname);
        return userInfo.orElse(null) != null;
    }

    @Transactional
    public Long join(String nickName, String loginId, String password) {

        User user = new User(nickName, loginId, password);
        user = userRepository.save(user);
        System.out.println(user.toString());

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
