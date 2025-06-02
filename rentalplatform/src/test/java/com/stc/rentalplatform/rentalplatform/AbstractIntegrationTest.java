package com.stc.rentalplatform.rentalplatform;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.shaded.com.google.common.net.HttpHeaders;
import org.testcontainers.utility.MountableFile;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public abstract class AbstractIntegrationTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:12")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");


    static GenericContainer<?> keycloak = new GenericContainer<>("quay.io/keycloak/keycloak:22.0")
            .withExposedPorts(8080)
            .withEnv("KEYCLOAK_ADMIN", "admin")
            .withEnv("KEYCLOAK_ADMIN_PASSWORD", "admin")
            .withCopyFileToContainer(
                    MountableFile.forClasspathResource("keycloak/test-realm.json"),
                    "/opt/keycloak/data/import/test-realm.json")
            .withCommand("start-dev --import-realm");

    @BeforeAll
    static void startContainers() {
        postgres.start();
        keycloak.start();
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
                () -> "http://" + keycloak.getHost() + ":" + keycloak.getMappedPort(8080) + "/keycloak/test-realm");

    }

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void cleanDatabase() throws Exception {
        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("TRUNCATE TABLE booking, property RESTART IDENTITY CASCADE");
        }
    }
    protected String getAccessToken() {
        WebClient client = WebClient.create();
        String url = "http://" + keycloak.getHost() + ":" + keycloak.getMappedPort(8080)
                + "/realms/test-realm/protocol/openid-connect/token";

        String response = client.post()
                .uri(url)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData("grant_type", "password")
                        .with("client_id", "rental-client")
                        .with("username", "testuser")
                        .with("password", "password"))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return JsonPath.read(response, "$.access_token");
    }
}
