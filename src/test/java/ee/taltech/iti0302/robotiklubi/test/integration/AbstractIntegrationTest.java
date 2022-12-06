package ee.taltech.iti0302.robotiklubi.test.integration;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

public class AbstractIntegrationTest {
    private static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER;
    static {
        POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:13.5");
        POSTGRESQL_CONTAINER.start();
    }

    @DynamicPropertySource
    private static void addDataSourceProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
        dynamicPropertyRegistry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);
    }
}
