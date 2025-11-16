package team.four.pas.repositories;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import team.four.pas.Config;
import team.four.pas.repositories.implementation.MongoAllocationRepository;

import java.io.File;

@Testcontainers
class AllocationRepositoryTest {
    @Container
    public static DockerComposeContainer<?> compose =
            new DockerComposeContainer<>(new File("src/test/resources/docker-compose.yml"))
                    .withExposedService("mongo", 27017);

    private MongoAllocationRepository allocationRepository;
    private AnnotationConfigApplicationContext context;

    @BeforeEach
    void each(){
        String host = compose.getServiceHost("mongo", 27017);
        Integer port = compose.getServicePort("mongo", 27017);
        String dynamicUri = "mongodb://" + host + ":" + port + "/pas";

        System.setProperty("pas.data.mongodb.uri", dynamicUri);

        context = new AnnotationConfigApplicationContext(Config.class);
        allocationRepository = context.getBean(MongoAllocationRepository.class);
    }

    @AfterEach
    void afterEach(){
        MongoDatabase database = context.getBean(MongoClient.class).getDatabase("pas");
        database.getCollection("vmAllocations").deleteMany(new Document());
    }

    @Test
    void getAll() {
        System.out.println(allocationRepository.getAll().getFirst());
    }

}