package com.odeyalo.grpc.books.api.grpc;

import com.odeyalo.grpc.books.client.book.Book.BookDto;
import com.odeyalo.grpc.books.client.book.Book.CreateBookRequest;
import com.odeyalo.grpc.books.client.book.Book.FetchBookRequest;
import com.odeyalo.grpc.books.entity.BookEntity;
import com.odeyalo.grpc.books.exception.BookNotFoundException;
import com.odeyalo.grpc.books.repository.BookRepository;
import com.odeyalo.grpc.books.repository.InMemoryBookRepository;
import com.odeyalo.grpc.books.service.BookService;
import com.odeyalo.grpc.books.support.converter.BookConverterImpl;
import com.odeyalo.grpc.books.support.converter.BookDtoConverterImpl;
import io.grpc.internal.testing.StreamRecorder;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import testing.faker.BookEntityFaker;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultGrpcBookServiceTest {


    @Nested
    class FetchBookByIdTest {
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

        @Test
        void shouldFetchBookByItsIdAndReturnCorrectIsbn() throws Exception {
            final DefaultGrpcBookService testable = TestableBuilder
                    .builder()
                    .withBooks(EXISTING_BOOK)
                    .build();

            String bookId = EXISTING_BOOK.getId().toString();

            FetchBookRequest fetchBookRequest = FetchBookRequest.newBuilder()
                    .setBookId(bookId)
                    .build();

            BookDto found = fetchBook(testable, fetchBookRequest);

            assertThat(found.getIsbn()).isEqualTo(EXISTING_BOOK.getIsbn());
        }

        @Test
        void shouldFetchBookByItsIdAndReturnCorrectAuthor() throws Exception {
            final DefaultGrpcBookService testable = TestableBuilder
                    .builder()
                    .withBooks(EXISTING_BOOK)
                    .build();

            String bookId = EXISTING_BOOK.getId().toString();

            FetchBookRequest fetchBookRequest = FetchBookRequest.newBuilder()
                    .setBookId(bookId)
                    .build();

            BookDto found = fetchBook(testable, fetchBookRequest);

            assertThat(found.getAuthor()).isEqualTo(EXISTING_BOOK.getAuthor());
        }

        @Test
        void shouldFetchBookByItsIdAndReturnCorrectQuantity() throws Exception {
            final DefaultGrpcBookService testable = TestableBuilder
                    .builder()
                    .withBooks(EXISTING_BOOK)
                    .build();

            String bookId = EXISTING_BOOK.getId().toString();

            FetchBookRequest fetchBookRequest = FetchBookRequest.newBuilder()
                    .setBookId(bookId)
                    .build();

            BookDto found = fetchBook(testable, fetchBookRequest);

            assertThat(found.getQuantity()).isEqualTo(EXISTING_BOOK.getQuantity());
        }

        @Test
        void shouldReturnNothingIfBookByIdDoesNotExist() throws Exception {
            DefaultGrpcBookService testable = TestableBuilder.builder().build();

            FetchBookRequest fetchBookRequest = FetchBookRequest.newBuilder()
                    .setBookId(UUID.randomUUID().toString())
                    .build();

            StreamRecorder<BookDto> recorder = StreamRecorder.create();

            testable.fetchBook(fetchBookRequest, recorder);

            recorder.awaitCompletion(5, TimeUnit.SECONDS);

            Throwable error = recorder.getError();

            assertThat(error).isInstanceOf(BookNotFoundException.class);
        }

        private static BookDto fetchBook(DefaultGrpcBookService testable, FetchBookRequest fetchBookRequest) throws Exception {
            StreamRecorder<BookDto> recorder = StreamRecorder.create();

            testable.fetchBook(fetchBookRequest, recorder);

            recorder.awaitCompletion(5, TimeUnit.SECONDS);

            return recorder.firstValue().get();
        }
    }

    @Nested
    class AddBookTest {
        public static final CreateBookRequest CREATE_NOVEL_REQUEST = CreateBookRequest.newBuilder()
                .setName("Three Days of Happiness")
                .setIsbn("12345677899")
                .setAuthor("Sugaru Miaki")
                .setQuantity(10)
                .build();

        @Test
        void shouldReturnSavedBookAsResponse() throws Exception {
            DefaultGrpcBookService testable = TestableBuilder.builder().build();
            StreamRecorder<BookDto> recorder = StreamRecorder.create();

            testable.addBook(CREATE_NOVEL_REQUEST, recorder);

            recorder.awaitCompletion(5, TimeUnit.SECONDS);

            assertThat(recorder.firstValue().get()).isNotNull();
        }

        @Test
        void shouldReturnSavedBookWithGeneratedId() throws Exception {
            DefaultGrpcBookService testable = TestableBuilder.builder().build();
            StreamRecorder<BookDto> recorder = StreamRecorder.create();

            testable.addBook(CREATE_NOVEL_REQUEST, recorder);

            recorder.awaitCompletion(5, TimeUnit.SECONDS);

            String bookId = recorder.firstValue().get().getId();

            assertThat(bookId).isNotEmpty();
        }

        @Test
        void shouldReturnSavedBookWithTitleAsProvided() throws Exception {
            DefaultGrpcBookService testable = TestableBuilder.builder().build();
            StreamRecorder<BookDto> recorder = StreamRecorder.create();

            testable.addBook(CREATE_NOVEL_REQUEST, recorder);

            recorder.awaitCompletion(5, TimeUnit.SECONDS);

            String title = recorder.firstValue().get().getName();

            assertThat(title).isEqualTo(CREATE_NOVEL_REQUEST.getName());
        }

        @Test
        void shouldReturnSavedBookWithIsbnAsProvided() throws Exception {
            DefaultGrpcBookService testable = TestableBuilder.builder().build();
            StreamRecorder<BookDto> recorder = StreamRecorder.create();

            testable.addBook(CREATE_NOVEL_REQUEST, recorder);

            recorder.awaitCompletion(5, TimeUnit.SECONDS);

            String Isbn = recorder.firstValue().get().getIsbn();

            assertThat(Isbn).isEqualTo(CREATE_NOVEL_REQUEST.getIsbn());
        }


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
            return new DefaultGrpcBookService(
                    new BookService(bookRepository, new BookConverterImpl()),
                    new BookDtoConverterImpl());
        }
    }
}