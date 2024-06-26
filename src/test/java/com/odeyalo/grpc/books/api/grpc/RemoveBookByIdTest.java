package com.odeyalo.grpc.books.api.grpc;

import com.odeyalo.grpc.books.api.grpc.Book.DeletionStatus;
import com.odeyalo.grpc.books.entity.BookEntity;
import com.odeyalo.grpc.books.exception.BookNotFoundException;
import com.odeyalo.grpc.books.exception.RequestValidationException;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import testing.faker.BookEntityFaker;

import java.util.UUID;

import static testing.factory.DefaultGrpcReactiveBookServiceTestableBuilder.testableBuilder;

class RemoveBookByIdTest {
    private static final String EXISTING_BOOK_ID = UUID.randomUUID().toString();

    private static final BookEntity EXISTING_BOOK = BookEntityFaker.create()
            .setId(UUID.fromString(EXISTING_BOOK_ID))
            .get();
    private static final Book.DeleteBookRequest DELETE_EXISTING_BOOK_REQUEST = Book.DeleteBookRequest.newBuilder()
            .setBookId(EXISTING_BOOK_ID)
            .build();

    private static final Book.DeleteBookRequest MALFORMED_DELETE_EXISTING_BOOK_REQUEST = Book.DeleteBookRequest.newBuilder()
            .setBookId("123")
            .build();

    private static final Book.FetchBookRequest FETCH_EXISTING_BOOK_REQUEST = Book.FetchBookRequest.newBuilder()
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

    @Test
    void shouldReturnErrorIfBookIdIsNotUUIDString() {
        // given
        DefaultReactiveBookService testable = testableBuilder().build();
        // when
        testable.removeBook(MALFORMED_DELETE_EXISTING_BOOK_REQUEST)
                .as(StepVerifier::create)
                .expectError(RequestValidationException.class)
                .verify();
    }

    @Test
    void shouldReturnSuccessCompletionStatusIfBookExist() {
        DefaultReactiveBookService testable = testableBuilder()
                .withBooks(EXISTING_BOOK)
                .build();

        testable.removeBook(DELETE_EXISTING_BOOK_REQUEST)
                .map(Book.DeleteBookResponse::getStatus)
                .as(StepVerifier::create)
                .expectNext(DeletionStatus.SUCCESS)
                .verifyComplete();
    }

    @Test
    void shouldActuallyDeleteTheBookFromService() {
        DefaultReactiveBookService testable = testableBuilder()
                .withBooks(EXISTING_BOOK)
                .build();

        testable.removeBook(DELETE_EXISTING_BOOK_REQUEST)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        testable.fetchBook(FETCH_EXISTING_BOOK_REQUEST)
                .as(StepVerifier::create)
                .expectError(BookNotFoundException.class)
                .verify();
    }
}
