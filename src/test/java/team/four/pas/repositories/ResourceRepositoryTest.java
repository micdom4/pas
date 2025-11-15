package team.four.pas.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import team.four.pas.Config;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void add() {
        assertTrue(resourceRepository.addVM(5, 12, 10));
    }

    /* RRR
       R  R
       RRR
       R  R
       R   R */

    @Test
    void findById() {

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