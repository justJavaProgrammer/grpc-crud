package com.odeyalo.grpc.books.repository;

import com.odeyalo.grpc.books.entity.BookEntity;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import testing.faker.BookEntityFaker;

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
}