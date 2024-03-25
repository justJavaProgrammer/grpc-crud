package com.odeyalo.grpc.books.api.grpc;

import com.odeyalo.grpc.books.entity.BookEntity;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import testing.faker.BookEntityFaker;

import java.util.UUID;

import static testing.factory.DefaultGrpcReactiveBookServiceTestableBuilder.testableBuilder;

class RemoveBookByIdTest extends AbstractBookClientTest {
    private static final String EXISTING_BOOK_ID = UUID.randomUUID().toString();
    private static final BookEntity EXISTING_BOOK = BookEntityFaker.create()
            .setId(UUID.fromString(EXISTING_BOOK_ID))
            .get();
    private static final Book.DeleteBookRequest DELETE_EXISTING_BOOK_REQUEST = Book.DeleteBookRequest.newBuilder()
            .setBookId(EXISTING_BOOK_ID)
            .build();

    @Test
    void shouldCompleteWithoutError() {
        DefaultReactiveBookService testable = testableBuilder()
                .withBooks(EXISTING_BOOK)
                .build();

        testable.removeBook(DELETE_EXISTING_BOOK_REQUEST)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }
}
