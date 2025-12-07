package team.four.pas;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.DockerComposeContainer;
import java.io.File;
import java.util.Map;

public class DockerComposeResource implements QuarkusTestResourceLifecycleManager {

    private static final DockerComposeContainer<?> COMPOSE =
            new DockerComposeContainer<>(new File("src/test/resources/docker-compose.yml"))
                    .withExposedService("mongo", 27017);

    @Override
    public Map<String, String> start() {
        COMPOSE.start();

        String host = COMPOSE.getServiceHost("mongo", 27017);
        Integer port = COMPOSE.getServicePort("mongo", 27017);
        String dynamicUri = "mongodb://" + host + ":" + port + "/pas";

        return Map.of("pas.data.mongodb.uri", dynamicUri);
    }

    @Override
    public void stop() {
        COMPOSE.stop();
    }
}