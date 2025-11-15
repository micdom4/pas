package team.four.pas.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import team.four.pas.Config;
import team.four.pas.data.users.Admin;
import team.four.pas.data.users.Client;
import team.four.pas.data.users.User;

import static org.junit.jupiter.api.Assertions.*;

class ResourceRepositoryTest {

    private ResourceRepository resourceRepository;
    private AnnotationConfigApplicationContext context;

    @BeforeEach
    void each(){
        context = new AnnotationConfigApplicationContext(Config.class);
        resourceRepository = context.getBean(ResourceRepository.class);
    }


  /* CCC
    C
    C
    C
     CCC  */

    @Test
    void addPassWhenFreeLogin() {
    }

    @Test
    void addFailWhenLoginExists() {
    }

    @Test
    void addFailWhenLoginEmpty() {
    }

    /* RRR
       R  R
       RRR
       R  R
       R   R */

    @Test
    void findByLogin() {
    }

    @Test
    void findByMatchingLogin() {
    }

    @Test
    void shouldReturnCorrectType() {
    }


    /* U   U
       U   U
       U   U
       U   U
        UUU  */

    @Test
    void updatePass() {
    }

    /* DDD
       D  D
       D  D
       D  D
       DDD  */

    @Test
    void delete() {
    }
}
}