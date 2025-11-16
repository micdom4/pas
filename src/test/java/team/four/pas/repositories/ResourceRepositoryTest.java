package team.four.pas.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import team.four.pas.Config;
import team.four.pas.services.data.resources.Resource;
import team.four.pas.services.data.resources.VirtualMachine;

import java.util.List;
import java.util.stream.Collectors;

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
        List<Resource> resources = resourceRepository.getAll();
        Resource resource = resources.getFirst();
        assertEquals(resource, resourceRepository.findById(resource.getId()));
    }

    /* U   U
       U   U
       U   U
       U   U
        UUU  */

    @Test
    void updatePass() {
        VirtualMachine vm = (VirtualMachine) resourceRepository.getAll().stream().filter(r -> r instanceof VirtualMachine).collect(Collectors.toList()).getFirst();
        int ramBefore = vm.getRamGiB();
        int memory = vm.getStorageGiB();
        int cpus = vm.getCpuNumber();

        assertNotEquals(-1, ramBefore);
        assertNotEquals(-1, memory);
        assertNotEquals(-1, cpus);

        assertTrue(resourceRepository.updateVM(vm.getId(), -1, -1 ,-1));

        assertEquals(-1, vm.getRamGiB());
        assertEquals(-1, vm.getStorageGiB());
        assertEquals(-1, vm.getCpuNumber());
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