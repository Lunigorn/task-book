package lunigorn.task_book_service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

@RunWith(SpringRunner.class)
@PropertySource("application-test.properties")
@SpringBootTest
public class TaskBookServiceApplicationTests {

	@Bean
	public PostgreSQLContainer postgresql() { return new PostgreSQLContainer();}
	
	@Test
	public void contextLoads() {
	}

}
