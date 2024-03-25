package com.odeyalo.grpc.books.api.grpc;

import com.odeyalo.grpc.books.api.grpc.Book.UpdateBookRequest;
import com.odeyalo.grpc.books.entity.BookEntity;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import testing.faker.BookEntityFaker;
import testing.faker.UpdateBookRequestFaker;

import java.util.UUID;

import static testing.factory.DefaultGrpcReactiveBookServiceTestableBuilder.testableBuilder;

class UpdateBookEndpointTest extends AbstractBookClientTest {
    private static final String EXISTING_BOOK_ID = UUID.randomUUID().toString();
    private static final BookEntity EXISTING_BOOK = BookEntityFaker.create().setId(UUID.fromString(EXISTING_BOOK_ID)).get();

    @Test
    void shouldCompleteWithoutAnyError() {
        // given
        DefaultReactiveBookService testable = testableBuilder()
                .withBooks(EXISTING_BOOK)
                .build();

        UpdateBookRequest updateBookRequest = UpdateBookRequestFaker.withId(EXISTING_BOOK_ID).get();

        // when
        testable.updateBook(updateBookRequest)
                .as(StepVerifier::create)
                // then
                .expectNextCount(1)
                .verifyComplete();
    }
}
