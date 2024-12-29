package shurona.wordfinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

// SpringBootApplication 안에 ComponentScan 들어있음
@EnableScheduling
@SpringBootApplication
public class WordFinderApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext ac = SpringApplication.run(WordFinderApplication.class, args);

//		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(WordFinderApplication.class);
//		for (String beanName : ac.getBeanDefinitionNames()) {
//			BeanDefinition beanDefinition = ac.getBeanFactory().getBeanDefinition(beanName);
//
//			if (beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
//				System.out.println("name = " + beanName);
//			}
//		}

	}

}
