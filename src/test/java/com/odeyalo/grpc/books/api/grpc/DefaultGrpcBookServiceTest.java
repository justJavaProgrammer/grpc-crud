package com.odeyalo.grpc.books.api.grpc;

import com.odeyalo.grpc.books.client.book.Book.BookDto;
import com.odeyalo.grpc.books.client.book.Book.FetchBookRequest;
import com.odeyalo.grpc.books.entity.BookEntity;
import com.odeyalo.grpc.books.repository.BookRepository;
import com.odeyalo.grpc.books.repository.InMemoryBookRepository;
import io.grpc.internal.testing.StreamRecorder;
import org.junit.jupiter.api.Test;
import testing.faker.BookEntityFaker;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultGrpcBookServiceTest {

    public static final BookEntity EXISTING_BOOK = BookEntityFaker.create().get();

    @Test
    void shouldFetchBookByItsId() throws Exception {
        final DefaultGrpcBookService testable = TestableBuilder
                .builder()
                .withBooks(EXISTING_BOOK)
                .build();

        String bookId = EXISTING_BOOK.getId().toString();

        FetchBookRequest fetchBookRequest = FetchBookRequest.newBuilder()
                .setBookId(bookId)
                .build();

        BookDto found = fetchBook(testable, fetchBookRequest);

        assertThat(found.getId()).isEqualTo(bookId);
    }

    @Test
    void shouldFetchBookByItsIdAndReturnCorrectName() throws Exception {
        final DefaultGrpcBookService testable = TestableBuilder
                .builder()
                .withBooks(EXISTING_BOOK)
                .build();

        String bookId = EXISTING_BOOK.getId().toString();

        FetchBookRequest fetchBookRequest = FetchBookRequest.newBuilder()
                .setBookId(bookId)
                .build();

        BookDto found = fetchBook(testable, fetchBookRequest);

        assertThat(found.getName()).isEqualTo(EXISTING_BOOK.getName());
    }

    private static BookDto fetchBook(DefaultGrpcBookService testable, FetchBookRequest fetchBookRequest) throws Exception {
        StreamRecorder<BookDto> recorder = StreamRecorder.create();

        testable.fetchBook(fetchBookRequest, recorder);

        recorder.awaitCompletion(5, TimeUnit.SECONDS);

        return recorder.firstValue().get();
    }


    static class TestableBuilder {
        private BookRepository bookRepository = new InMemoryBookRepository();

        public static TestableBuilder builder() {
            return new TestableBuilder();
        }

        public TestableBuilder withBooks(BookEntity... books) {
            bookRepository = new InMemoryBookRepository(books);
            return this;
        }

        public DefaultGrpcBookService build() {
            return new DefaultGrpcBookService(bookRepository);
        }
    }
}