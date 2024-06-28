package shurona.wordfinder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;

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
