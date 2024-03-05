package shurona.wordfinder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import shurona.wordfinder.repository.MemoryUserRepository;
import shurona.wordfinder.repository.UserRepository;
import shurona.wordfinder.service.UserService;

@Configuration
@ComponentScan
public class SpringConfig {

//    @Bean
//    public UserService userService() {
//        return new UserService(memoryUserRepository());
//    }
//
//    @Bean
//    public UserRepository memoryUserRepository() {
//        return new MemoryUserRepository();
//    }
}
