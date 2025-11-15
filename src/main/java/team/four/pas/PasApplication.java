package team.four.pas;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import team.four.pas.repositories.UserRepository;

public class PasApplication {

	public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(Config.class);

        UserRepository userRepository = context.getBean(UserRepository.class);

	}

}
