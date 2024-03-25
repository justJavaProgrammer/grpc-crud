package com.odeyalo.grpc.books.api.grpc;

import com.odeyalo.grpc.books.api.grpc.Book.UpdateBookRequest;
import com.odeyalo.grpc.books.api.grpc.Book.UpdateBookRequest.UpdateBookPayload;
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
        UpdateBookPayload payload = UpdateBookPayloadFaker.create().setTitle("").get();

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

    @Test
    void shouldReturnErrorIfBookIsbnIsLessThan10Symbols() {
        // given
        DefaultReactiveBookService testable = testableBuilder().build();
        UpdateBookPayload payload = UpdateBookPayloadFaker.create()
                .setIsbn("12345")
                .get();

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

    @Test
    void shouldReturnErrorIfBookIsbnIsLargerThan15Symbols() {
        // given
        DefaultReactiveBookService testable = testableBuilder().build();
        UpdateBookPayload payload = UpdateBookPayloadFaker.create()
                .setIsbn("very_long_string_that_is_longer_than_15_symbols")
                .get();

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

    @Test
    void shouldReturnErrorIfBookAuthorIsLessThan5Symbols() {
        // given
        DefaultReactiveBookService testable = testableBuilder().build();
        UpdateBookPayload payload = UpdateBookPayloadFaker.create()
                .setAuthorName("less")
                .get();

        UpdateBookRequest malformedUpdateBookRequest = UpdateBookRequest.newBuilder()
                .setBookId(EXISTING_BOOK_ID)
                .setNewBook(payload)
                .build();
        // when
        testable.updateBook(malformedUpdateBookRequest)
                .as(StepVerifier::create)
                // then
                .expectError(RequestValidationException.class)
                .verify();
    }

    @Test
    void shouldReturnErrorIfQuantityIsLessThanZero() {
        // given
        DefaultReactiveBookService testable = testableBuilder().build();
        UpdateBookPayload payload = UpdateBookPayloadFaker.create()
                .setQuantity(-1)
                .get();

        UpdateBookRequest malformedUpdateBookRequest = UpdateBookRequest.newBuilder()
                .setBookId(EXISTING_BOOK_ID)
                .setNewBook(payload)
                .build();
        // when
        testable.updateBook(malformedUpdateBookRequest)
                .as(StepVerifier::create)
                // then
                .expectError(RequestValidationException.class)
                .verify();
    }

    @Test
    void shouldReturnNameOfTheBookThatWasProvidedInPayload() {
        // given
        DefaultReactiveBookService testable = testableBuilder()
                .withBooks(EXISTING_BOOK)
                .build();

        UpdateBookRequest updateBookRequest = UpdateBookRequestFaker.withId(EXISTING_BOOK_ID).get();

        // when
        testable.updateBook(updateBookRequest)
                .map(Book.BookDto::getName)
                .as(StepVerifier::create)
                .expectNext(updateBookRequest.getNewBook().getName())
                .verifyComplete();
    }

    @Test
    void shouldReturnAuthorOfTheBookThatWasProvidedInPayload() {
        // given
        DefaultReactiveBookService testable = testableBuilder()
                .withBooks(EXISTING_BOOK)
                .build();

        UpdateBookRequest updateBookRequest = UpdateBookRequestFaker.withId(EXISTING_BOOK_ID).get();

        // when
        testable.updateBook(updateBookRequest)
                .map(Book.BookDto::getAuthor)
                .as(StepVerifier::create)
                .expectNext(updateBookRequest.getNewBook().getAuthor())
                .verifyComplete();
    }

    @Test
    void shouldReturnIsbnOfTheBookThatWasProvided() {
        // given
        DefaultReactiveBookService testable = testableBuilder()
                .withBooks(EXISTING_BOOK)
                .build();

        UpdateBookRequest updateBookRequest = UpdateBookRequestFaker.withId(EXISTING_BOOK_ID).get();
        // when
        testable.updateBook(updateBookRequest)
                .map(Book.BookDto::getIsbn)
                .as(StepVerifier::create)
                // then
                .expectNext(updateBookRequest.getNewBook().getIsbn())
                .verifyComplete();
    }

    @Test
    void shouldReturnQuantityOfTheBookThatWasProvidedInPayload() {
        // given
        DefaultReactiveBookService testable = testableBuilder()
                .withBooks(EXISTING_BOOK)
                .build();

        UpdateBookRequest updateBookRequest = UpdateBookRequestFaker.withId(EXISTING_BOOK_ID).get();

        // when
        testable.updateBook(updateBookRequest)
                .map(Book.BookDto::getQuantity)
                .as(StepVerifier::create)
                .expectNext(updateBookRequest.getNewBook().getQuantity())
                .verifyComplete();
    }

    @Test
    void shouldReturnIdOfTheBookThatWasRequested() {
        // given
        DefaultReactiveBookService testable = testableBuilder()
                .withBooks(EXISTING_BOOK)
                .build();

        UpdateBookRequest updateBookRequest = UpdateBookRequestFaker.withId(EXISTING_BOOK_ID).get();

        // when
        testable.updateBook(updateBookRequest)
                .map(Book.BookDto::getId)
                .as(StepVerifier::create)
                // then
                .expectNext(updateBookRequest.getBookId())
                .verifyComplete();
    }
}
