package shurona.wordfinder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import shurona.wordfinder.custom.interceptor.HeaderAuthForDev;
import shurona.wordfinder.user.interceoptor.LoginCheckInterceptor;
import shurona.wordfinder.user.repository.DatabaseUserRepository;
import shurona.wordfinder.user.repository.MemoryUserRepository;
import shurona.wordfinder.user.repository.UserRepository;
import shurona.wordfinder.user.service.UserService;

import javax.sql.DataSource;


@Configuration
public class SpringConfig implements WebMvcConfigurer {

    private final HeaderAuthForDev headerAuthForDev;

    public SpringConfig(HeaderAuthForDev headerAuthForDev) {
        this.headerAuthForDev = headerAuthForDev;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/home", "/user/new", "/login",
                        "/logout", "/css/**", "/*.ico", "/error", "/quiz/test", "/connection/**");

        registry.addInterceptor(this.headerAuthForDev).order(2)
                .addPathPatterns("/connection/**");
    }

    //    @Bean
    public UserService userService() {
        return new UserService(userRepository());
    }
//
    @Bean
    public UserRepository userRepository() {
//        return new MemoryUserRepository();
        return new DatabaseUserRepository();
    }

    @Bean
    // 테스트가 아닌 경우에만 등록
    @Profile("!test")
    public TestDataInit testDataInit() {
        return new TestDataInit(this.userRepository());
    }


}
