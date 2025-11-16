package team.four.pas.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import team.four.pas.Config;
import team.four.pas.services.data.users.Admin;
import team.four.pas.services.data.users.Client;
import team.four.pas.services.data.users.User;

import static org.junit.jupiter.api.Assertions.*;


class LocalUserRepositoryTest {

    private UserRepository userRepository;
    private AnnotationConfigApplicationContext context;

    @BeforeEach
    void each(){
        context = new AnnotationConfigApplicationContext(Config.class);
        userRepository = context.getBean(UserRepository.class);
    }


  /* CCC
    C
    C
    C
     CCC  */

    @Test
    void addPassWhenFreeLogin() {
        assertTrue(userRepository.add("BLis2", "Bartosz", "Lis", Client.class));
    }

    @Test
    void addFailWhenLoginExists() {
        assertFalse(userRepository.add("BLis", "Bartosz", "Lis", Client.class));
    }

    @Test
    void addFailWhenLoginEmpty() {
        assertFalse(userRepository.add("", "Bartosz", "Lis", Client.class));
    }

    /* RRR
       R  R
       RRR
       R  R
       R   R */

    @Test
    void findByLogin() {
        assertEquals("Bartosz", userRepository.findByLogin("BLis").getName());
    }

    @Test
    void findByMatchingLogin() {
        assertEquals("Bartosz", userRepository.findByMatchingLogin("BL").getFirst().getName());
    }

    @Test
    void shouldReturnCorrectType() {
        User user = userRepository.findByLogin("BLis");
        assertEquals(Admin.class, user.getClass());
    }


    /* U   U
       U   U
       U   U
       U   U
        UUU  */

    @Test
    void updatePass() {
        assertEquals("Lis", userRepository.findByLogin("BLis").getSurname());
        assertTrue(userRepository.updateByLogin("BLis", "Lis-Nowak"));
        assertEquals("Lis-Nowak", userRepository.findByLogin("BLis").getSurname());
    }

    /* DDD
       D  D
       D  D
       D  D
       DDD  */

    @Test
    void delete() {
        assertEquals("Lis", userRepository.findByLogin("BLis").getSurname());
        assertTrue(userRepository.delete("BLis"));
        assertNull(userRepository.findByLogin("BLis"));
    }
}