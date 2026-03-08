package team.four.pas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "team.four.pas.repositories")
public class PasApplication {

	public static void main(String[] args) {
        SpringApplication.run(PasApplication.class, args);
	}
}
