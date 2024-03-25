package com.odeyalo.grpc.books.api.grpc;

import com.odeyalo.grpc.books.entity.BookEntity;
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
}
