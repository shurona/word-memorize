package shurona.wordfinder;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;

// SpringBootApplication 안에 ComponentScan 들어있음
@SpringBootApplication
public class WordFinderApplication {
	public static void main(String[] args) {
		SpringApplication.run(WordFinderApplication.class, args);

		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(WordFinderApplication.class);
//		for (String beanName : ac.getBeanDefinitionNames()) {
//			BeanDefinition beanDefinition = ac.getBeanDefinition(beanName);
//
//			if (beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
//				System.out.println("name = " + beanName);
//			}
//		}

	}

}
