package com.odeyalo.grpc.books.api.grpc;

import com.odeyalo.grpc.books.api.grpc.Book.UpdateBookRequest;
import com.odeyalo.grpc.books.entity.BookEntity;
import com.odeyalo.grpc.books.exception.RequestValidationException;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import testing.faker.BookEntityFaker;
import testing.faker.UpdateBookPayloadFaker;
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

    @Test
    void shouldReturnValidationErrorIfIdIsNotUUID() {
        // given
        DefaultReactiveBookService testable = testableBuilder().build();
        UpdateBookRequest updateBookRequest = UpdateBookRequest.newBuilder().setBookId("123").build();
        // when
        testable.updateBook(updateBookRequest)
                .as(StepVerifier::create)
                // then
                .expectError(RequestValidationException.class)
                .verify();
    }

    @Test
    void shouldReturnErrorIfBookNameIsLessThan1Symbol() {
        // given
        DefaultReactiveBookService testable = testableBuilder().build();
        UpdateBookRequest.UpdateBookPayload payload = UpdateBookPayloadFaker.create().setTitle("").get();

        UpdateBookRequest malformedUpdateBookRequest = UpdateBookRequest.newBuilder()
                .setBookId(EXISTING_BOOK_ID)
                .setNewBook(payload)
                .build();
        // when
        testable.updateBook(malformedUpdateBookRequest)
                .as(StepVerifier::create)
                .expectError(RequestValidationException.class)
                .verify();
    }
}
