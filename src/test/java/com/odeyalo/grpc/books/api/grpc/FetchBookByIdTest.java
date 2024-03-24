package com.odeyalo.grpc.books.api.grpc;

import com.odeyalo.grpc.books.entity.BookEntity;
import com.odeyalo.grpc.books.exception.BookNotFoundException;
import com.odeyalo.grpc.books.exception.RequestValidationException;
import io.grpc.internal.testing.StreamRecorder;
import org.junit.jupiter.api.Test;
import testing.faker.BookEntityFaker;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static testing.factory.DefaultGrpcBookServiceTestableBuilder.testableBuilder;

class FetchBookByIdTest extends AbstractBookClientTest {
    public static final BookEntity EXISTING_BOOK = BookEntityFaker.create().get();

    @Test
    void shouldFetchBookByItsId() throws Exception {
        final DefaultGrpcBookService testable = testableBuilder()
                .withBooks(EXISTING_BOOK)
                .build();

        String bookId = EXISTING_BOOK.getId().toString();

        Book.BookDto found = fetchBookAndGetResponsePayload(testable, bookId);

        assertThat(found.getId()).isEqualTo(bookId);
    }

    @Test
    void shouldReturnErrorIfNonUuidIsUsedAsId() throws Exception {
        DefaultGrpcBookService testable = testableBuilder().build();

        StreamRecorder<Book.BookDto> recorder = fetchBook(testable, "123");


        recorder.awaitCompletion(5, TimeUnit.SECONDS);

        assertThat(recorder.getError()).isInstanceOf(RequestValidationException.class);
    }

    @Test
    void shouldFetchBookByItsIdAndReturnCorrectName() throws Exception {
        final DefaultGrpcBookService testable = testableBuilder()
                .withBooks(EXISTING_BOOK)
                .build();

        String bookId = EXISTING_BOOK.getId().toString();

        Book.BookDto found = fetchBookAndGetResponsePayload(testable, bookId);

        assertThat(found.getName()).isEqualTo(EXISTING_BOOK.getName());
    }

    @Test
    void shouldFetchBookByItsIdAndReturnCorrectIsbn() throws Exception {
        final DefaultGrpcBookService testable = testableBuilder()
                .withBooks(EXISTING_BOOK)
                .build();

        String bookId = EXISTING_BOOK.getId().toString();

        Book.BookDto found = fetchBookAndGetResponsePayload(testable, bookId);

        assertThat(found.getIsbn()).isEqualTo(EXISTING_BOOK.getIsbn());
    }

    @Test
    void shouldFetchBookByItsIdAndReturnCorrectAuthor() throws Exception {
        final DefaultGrpcBookService testable = testableBuilder()
                .withBooks(EXISTING_BOOK)
                .build();

        String bookId = EXISTING_BOOK.getId().toString();

        Book.BookDto found = fetchBookAndGetResponsePayload(testable, bookId);

        assertThat(found.getAuthor()).isEqualTo(EXISTING_BOOK.getAuthor());
    }

    @Test
    void shouldFetchBookByItsIdAndReturnCorrectQuantity() throws Exception {
        final DefaultGrpcBookService testable = testableBuilder()
                .withBooks(EXISTING_BOOK)
                .build();

        String bookId = EXISTING_BOOK.getId().toString();

        Book.BookDto found = fetchBookAndGetResponsePayload(testable, bookId);

        assertThat(found.getQuantity()).isEqualTo(EXISTING_BOOK.getQuantity());
    }

    @Test
    void shouldReturnNothingIfBookByIdDoesNotExist() throws Exception {
        DefaultGrpcBookService testable = testableBuilder().build();

        StreamRecorder<Book.BookDto> recorder = fetchNotExistingBook(testable);

        assertThat(recorder.getError()).isInstanceOf(BookNotFoundException.class);
    }

    private StreamRecorder<Book.BookDto> fetchNotExistingBook(DefaultGrpcBookService testable) throws Exception {
        return super.fetchBook(testable, UUID.randomUUID().toString());
    }
}
