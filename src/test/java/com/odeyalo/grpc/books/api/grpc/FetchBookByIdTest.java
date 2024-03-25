package com.odeyalo.grpc.books.api.grpc;

import com.odeyalo.grpc.books.entity.BookEntity;
import com.odeyalo.grpc.books.exception.BookNotFoundException;
import com.odeyalo.grpc.books.exception.RequestValidationException;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import testing.faker.BookEntityFaker;

import java.util.UUID;

import static testing.factory.DefaultGrpcReactiveBookServiceTestableBuilder.testableBuilder;

class FetchBookByIdTest extends AbstractBookClientTest {
    private static final String EXISTING_BOOK_ID = UUID.randomUUID().toString();
    private static final BookEntity EXISTING_BOOK = BookEntityFaker.create()
            .setIdString(EXISTING_BOOK_ID)
            .get();
    private static final Book.FetchBookRequest FETCH_EXISTING_BOOK_REQUEST = Book.FetchBookRequest.newBuilder().setBookId(EXISTING_BOOK_ID).build();
    private static final Book.FetchBookRequest FETCH_NOT_EXISTING_BOOK_REQUEST = Book.FetchBookRequest.newBuilder().setBookId(UUID.randomUUID().toString()).build();
    private static final Book.FetchBookRequest MALFORMED_FETCH_BOOK_REQUEST = Book.FetchBookRequest.newBuilder().setBookId("123").build();

    @Test
    void shouldFetchBookByItsId() {
        final DefaultReactiveBookService testable = testableBuilder()
                .withBooks(EXISTING_BOOK)
                .build();

        testable.fetchBook(FETCH_EXISTING_BOOK_REQUEST)
                .map(Book.BookDto::getId)
                .as(StepVerifier::create)
                .expectNext(EXISTING_BOOK_ID)
                .verifyComplete();
    }

    @Test
    void shouldReturnErrorIfNonUuidIsUsedAsId() {
        DefaultReactiveBookService testable = testableBuilder().build();

        testable.fetchBook(MALFORMED_FETCH_BOOK_REQUEST)
                .map(Book.BookDto::getId)
                .as(StepVerifier::create)
                .expectError(RequestValidationException.class)
                .verify();
    }

    @Test
    void shouldReturnBookNotFoundErrorIfBookDoesNotExist() {
        DefaultReactiveBookService testable = testableBuilder().build();

        testable.fetchBook(FETCH_NOT_EXISTING_BOOK_REQUEST)
                .map(Book.BookDto::getId)
                .as(StepVerifier::create)
                .expectError(BookNotFoundException.class)
                .verify();
    }

    @Test
    void shouldFetchBookByItsIdAndReturnCorrectName() {
        DefaultReactiveBookService testable = testableBuilder()
                .withBooks(EXISTING_BOOK)
                .build();

        testable.fetchBook(FETCH_EXISTING_BOOK_REQUEST)
                .map(Book.BookDto::getName)
                .as(StepVerifier::create)
                .expectNext(EXISTING_BOOK.getName())
                .verifyComplete();
    }

    @Test
    void shouldFetchBookByItsIdAndReturnCorrectIsbn() {
        DefaultReactiveBookService testable = testableBuilder()
                .withBooks(EXISTING_BOOK)
                .build();

        testable.fetchBook(FETCH_EXISTING_BOOK_REQUEST)
                .map(Book.BookDto::getIsbn)
                .as(StepVerifier::create)
                .expectNext(EXISTING_BOOK.getIsbn())
                .verifyComplete();
    }


    @Test
    void shouldFetchBookByItsIdAndReturnCorrectAuthor() {
        DefaultReactiveBookService testable = testableBuilder()
                .withBooks(EXISTING_BOOK)
                .build();

        testable.fetchBook(FETCH_EXISTING_BOOK_REQUEST)
                .map(Book.BookDto::getAuthor)
                .as(StepVerifier::create)
                .expectNext(EXISTING_BOOK.getAuthor())
                .verifyComplete();
    }

    @Test
    void shouldFetchBookByItsIdAndReturnCorrectQuantity() {
        DefaultReactiveBookService testable = testableBuilder()
                .withBooks(EXISTING_BOOK)
                .build();

        testable.fetchBook(FETCH_EXISTING_BOOK_REQUEST)
                .map(Book.BookDto::getQuantity)
                .as(StepVerifier::create)
                .expectNext(EXISTING_BOOK.getQuantity())
                .verifyComplete();
    }
}
