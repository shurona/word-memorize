package shurona.wordfinder;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import shurona.wordfinder.user.repository.UserRepository;

//@Component
public class TestDataInit {

    private final UserRepository userRepository;

    @Autowired
    public TestDataInit(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
//        userRepository.save(new User("Shurona", "shurona", "test!"));
//        userRepository.save(new User("iron", "iron", "test!"));
    }

}
