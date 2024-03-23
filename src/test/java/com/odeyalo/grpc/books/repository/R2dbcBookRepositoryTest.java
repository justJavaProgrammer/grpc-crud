package com.odeyalo.grpc.books.repository;

import com.odeyalo.grpc.books.entity.BookEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;
import testing.faker.BookEntityFaker;

import java.util.UUID;

@DataR2dbcTest
@Testcontainers
@ActiveProfiles("test")
@Import(R2dbcBookRepository.class)
class R2dbcBookRepositoryTest extends DatabaseTest {

    @Autowired
    R2dbcBookRepository testable;

    @Test
    void shouldSaveEntity() {
        BookEntity bookEntity = BookEntityFaker.create()
                .setId(null)
                .get();

        testable.save(bookEntity)
                .as(StepVerifier::create)
                .expectNextMatches(it -> it.getId() != null)
                .verifyComplete();
    }

    @Test
    void shouldBeFoundAfterSave() {
        final BookEntity bookEntity = BookEntityFaker.create()
                .setId(null)
                .get();

        testable.save(bookEntity)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        testable.findBookById(bookEntity.getId())
                .as(StepVerifier::create)
                .expectNext(bookEntity)
                .verifyComplete();
    }

    @Test
    void shouldReturnNothingIfBookDoesNotExist() {
        testable.findBookById(UUID.randomUUID())
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void shouldRemoveEntityById() {
        final BookEntity bookEntity = BookEntityFaker.create()
                .setId(null)
                .get();

        testable.save(bookEntity)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        testable.removeById(bookEntity.getId())
                .as(StepVerifier::create)
                .verifyComplete();

        testable.findBookById(bookEntity.getId())
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void shouldRemoveNothingIfNotExistById() {
        final BookEntity bookEntity = BookEntityFaker.create()
                .setId(null)
                .get();

        testable.save(bookEntity)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        testable.removeById(UUID.randomUUID())
                .as(StepVerifier::create)
                .verifyComplete();

        testable.findBookById(bookEntity.getId())
                .as(StepVerifier::create)
                .expectNext(bookEntity)
                .verifyComplete();
    }
}