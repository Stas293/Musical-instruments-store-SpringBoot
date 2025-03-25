package org.projects.instrumentservice.integration;

import org.projects.instrumentservice.integration.annotation.IT;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

@IT
//@WithMockUser(username = "test@gmail.com", password = "test", authorities = {"ADMIN", "USER"})
public abstract class IntegrationTestBase {
    @Container
    private static final MongoDBContainer container = new MongoDBContainer(DockerImageName.parse("mongo:latest"));

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", container::getReplicaSetUrl);
    }
}
