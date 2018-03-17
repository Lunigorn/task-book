package lunigorn.task_book_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import liquibase.integration.spring.SpringLiquibase;

@Configuration
public class MigrationConfig {
	
//	@Bean
//	@DependsOn("dataSource")
//	SpringLiquibase liquibase() {
//		SpringLiquibase liquibase = new SpringLiquibase();
//		liquibase.setChangeLog("classpath:db/migration/changelog.xml");
//		return liquibase;
//	}

}
