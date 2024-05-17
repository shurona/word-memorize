package shurona.wordfinder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import shurona.wordfinder.user.interceoptor.LoginCheckInterceptor;
import shurona.wordfinder.user.repository.MemoryUserRepository;
import shurona.wordfinder.user.repository.UserRepository;
import shurona.wordfinder.user.service.UserService;

@Configuration
public class SpringConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/home", "/user/new", "/login", "/logout", "/css/**", "/*.ico", "/error");
    }

    //    @Bean
    public UserService userService() {
        return new UserService(new MemoryUserRepository());
    }
//
    @Bean
    public UserRepository memoryUserRepository() {
        return new MemoryUserRepository();
    }
}
