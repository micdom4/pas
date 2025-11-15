package team.four.pas.repositories;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import team.four.pas.Config;

import static org.junit.jupiter.api.Assertions.assertEquals;


class LocalUserRepositoryTest {

    private static UserRepository userRepository;
    private static AnnotationConfigApplicationContext context;

    @BeforeAll
    static void init(){
        context = new AnnotationConfigApplicationContext(Config.class);
    }

    @BeforeEach
    void each(){
        userRepository = context.getBean(UserRepository.class);
    }

    @Test
    void findByMatchingLogin() {
        assertEquals("BLis", userRepository.findByMatchingLogin("BL").getFirst().getLogin());
    }

    void add() {
    }

    void update() {
    }

    void delete() {
    }
}