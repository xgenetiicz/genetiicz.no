package com.example.genetiicz.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProjectIntegrationTest {

    //An instance of the static container(database) and we create an object of it.
        @Container
        @ServiceConnection
        static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));


        // https://www.youtube.com/watch?v=erp-7MCK5BU --> goated guy

    private WebTestClient webTestClient;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        this.webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    void uploadImage_shouldReturn401_whenNotAuthenticated() throws Exception {
        webTestClient.get()
                .uri("http://localhost:" + port + "/api/projects/upload/image/1")
                .exchange()
                .expectStatus().isUnauthorized(); //So WebTestClient is the new method by TESTING api call with the method too see if it actually works.
        //RestTemplate isn't used anymore and assertions is already built in.
    }
}
