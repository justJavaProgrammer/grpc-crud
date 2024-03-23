package com.odeyalo.grpc.books.repository;

import com.odeyalo.grpc.books.entity.BookEntity;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import testing.faker.BookEntityFaker;

import java.util.UUID;

class InMemoryBookRepositoryTest {

    @Test
    void shouldReturnExistingBookById() {
        BookEntity book = BookEntityFaker.create().get();

        var testable = InMemoryBookRepository.withBooks(book);

        testable.findBookById(book.getId())
                .as(StepVerifier::create)
                .expectNext(book)
                .verifyComplete();
    }

    @Test
    void shouldReturnNothingIfBookDoesNotExist() {

        var testable = InMemoryBookRepository.empty();

        testable.findBookById(UUID.randomUUID())
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void shouldReturnNothingIfNullIsProvided() {

        var testable = InMemoryBookRepository.empty();

        testable.findBookById(null)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void shouldSaveBook() {
        // given
        BookEntity book = BookEntityFaker.create().get();

        var testable = InMemoryBookRepository.empty();
        // when
        testable.save(book)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
        // then
        testable.findBookById(book.getId())
                .as(StepVerifier::create)
                .expectNext(book)
                .verifyComplete();
    }

    @Test
    void shouldGenerateIdForBookIfIdIsNull() {
        // given
        BookEntity book = BookEntityFaker.create().setId(null).get();

        var testable = InMemoryBookRepository.empty();
        // when
        testable.save(book)
                .as(StepVerifier::create)
                .expectNextMatches(it -> it.getId() != null)
                .verifyComplete();
        // then
        // will fail if Book became immutable. Should change it
        testable.findBookById(book.getId())
                .as(StepVerifier::create)
                .expectNext(book)
                .verifyComplete();
    }

    @Test
    void shouldRemoveBookByItsId() {
        // given
        BookEntity book = BookEntityFaker.create().get();

        var testable = InMemoryBookRepository.withBooks(book);
        // when
        testable.removeById(book.getId())
                .as(StepVerifier::create)
                .verifyComplete();

        // then expect nothing to return
        testable.findBookById(book.getId())
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void shouldRemoveNothingBookIfIdDoesNotExist() {
        // given
        BookEntity book = BookEntityFaker.create().get();

        var testable = InMemoryBookRepository.withBooks(book);
        // when
        testable.removeById(UUID.randomUUID())
                .as(StepVerifier::create)
                .verifyComplete();

        // then expect nothing to return
        testable.findBookById(book.getId())
                .as(StepVerifier::create)
                .expectNext(book)
                .verifyComplete();
    }

    @Test
    void shouldRemoveNothingBookIfNullIsProvided() {
        // given
        BookEntity book = BookEntityFaker.create().get();

        var testable = InMemoryBookRepository.withBooks(book);
        // when
        testable.removeById(null)
                .as(StepVerifier::create)
                .verifyComplete();

        // then expect nothing to return
        testable.findBookById(book.getId())
                .as(StepVerifier::create)
                .expectNext(book)
                .verifyComplete();
    }
}