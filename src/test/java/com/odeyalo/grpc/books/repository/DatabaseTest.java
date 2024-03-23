package com.odeyalo.grpc.books.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

/**
 * Set up the required infrastructure for database tests
 */
public class DatabaseTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:latest"
    );

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", () -> r2dbcUrl());
        registry.add("spring.r2dbc.username", postgres::getUsername);
        registry.add("spring.r2dbc.password", postgres::getPassword);
        registry.add("spring.flyway.url", postgres::getJdbcUrl);
        registry.add("spring.spring.flyway.user.username", postgres::getUsername);
        registry.add("spring.flyway.password", postgres::getPassword);
    }

    public static String r2dbcUrl() {
        return String.format("r2dbc:postgresql://%s:%d/%s", postgres.getHost(), postgres.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT), postgres.getDatabaseName());
    }
}
