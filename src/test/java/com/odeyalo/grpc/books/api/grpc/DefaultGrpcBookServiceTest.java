package com.odeyalo.grpc.books.api.grpc;

import com.odeyalo.grpc.books.api.grpc.Book.BookDto;
import com.odeyalo.grpc.books.api.grpc.Book.CreateBookRequest;
import com.odeyalo.grpc.books.api.grpc.Book.FetchBookRequest;
import com.odeyalo.grpc.books.api.grpc.Book.UpdateBookRequest;
import com.odeyalo.grpc.books.entity.BookEntity;
import com.odeyalo.grpc.books.exception.BookNotFoundException;
import com.odeyalo.grpc.books.repository.BookRepository;
import com.odeyalo.grpc.books.repository.InMemoryBookRepository;
import com.odeyalo.grpc.books.service.BookService;
import com.odeyalo.grpc.books.support.converter.BookConverterImpl;
import com.odeyalo.grpc.books.support.converter.BookDtoConverterImpl;
import com.odeyalo.grpc.books.support.converter.CreateBookInfoConverterImpl;
import com.odeyalo.grpc.books.support.converter.UpdateBookInfoConverterImpl;
import io.grpc.internal.testing.StreamRecorder;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import testing.faker.BookEntityFaker;
import testing.faker.UpdateBookRequestFaker;

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
        void shouldReturnSavedBookWithGeneratedId() throws Exception {
            DefaultGrpcBookService testable = TestableBuilder.builder().build();
            // when
            BookDto book = saveBook(testable);
            // then
            assertThat(book.getId()).isNotEmpty();
        }

        @Test
        void shouldReturnSavedBookWithTitleAsProvided() throws Exception {
            DefaultGrpcBookService testable = TestableBuilder.builder().build();
            // when
            BookDto book = saveBook(testable);
            // then
            assertThat(book.getName()).isEqualTo(CREATE_NOVEL_REQUEST.getName());
        }

        @Test
        void shouldReturnSavedBookWithIsbnAsProvided() throws Exception {
            DefaultGrpcBookService testable = TestableBuilder.builder().build();
            // when
            BookDto book = saveBook(testable);
            // then
            assertThat(book.getIsbn()).isEqualTo(CREATE_NOVEL_REQUEST.getIsbn());
        }

        @Test
        void shouldReturnSavedBookWithAuthorAsProvided() throws Exception {
            DefaultGrpcBookService testable = TestableBuilder.builder().build();
            // when
            BookDto book = saveBook(testable);
            // then
            assertThat(book.getAuthor()).isEqualTo(CREATE_NOVEL_REQUEST.getAuthor());
        }

        @Test
        void shouldReturnSavedBookWithQuantityAsProvided() throws Exception {
            DefaultGrpcBookService testable = TestableBuilder.builder().build();
            // when
            BookDto book = saveBook(testable);
            // then
            assertThat(book.getQuantity()).isEqualTo(CREATE_NOVEL_REQUEST.getQuantity());
        }

        @Test
        void bookShouldBeFoundAfterSave() throws Exception {
            DefaultGrpcBookService testable = TestableBuilder.builder().build();
            // when
            BookDto savedBook = saveBook(testable);

            // then
            BookDto bookDto = fetchBook(testable, savedBook);

            assertThat(bookDto).isEqualTo(savedBook);
        }

        private BookDto fetchBook(DefaultGrpcBookService testable, BookDto savedBook) throws Exception {
            StreamRecorder<BookDto> fetchBookRecorder = StreamRecorder.create();

            FetchBookRequest fetchBookRequest = FetchBookRequest.newBuilder().setBookId(savedBook.getId()).build();

            testable.fetchBook(fetchBookRequest, fetchBookRecorder);

            fetchBookRecorder.awaitCompletion(5, TimeUnit.SECONDS);

            return fetchBookRecorder.firstValue().get();
        }

        private BookDto saveBook(DefaultGrpcBookService testable) throws Exception {
            StreamRecorder<BookDto> recorder = StreamRecorder.create();

            testable.addBook(CREATE_NOVEL_REQUEST, recorder);

            recorder.awaitCompletion(5, TimeUnit.SECONDS);

            return recorder.firstValue().get();
        }
    }

    @Nested
    class UpdateBookTest {
        public static final BookEntity EXISTING_BOOK = BookEntityFaker.create().get();

        @Test
        void shouldCompleteWithoutAnyError() throws Exception {
            // given
            DefaultGrpcBookService testable = TestableBuilder.builder()
                    .withBooks(EXISTING_BOOK)
                    .build();
            UpdateBookRequest updateBookRequest = UpdateBookRequestFaker.withId(EXISTING_BOOK.getId()).get();

            // when
            StreamRecorder<BookDto> recorder = StreamRecorder.create();

            testable.updateBook(updateBookRequest, recorder);

            recorder.awaitCompletion(5, TimeUnit.SECONDS);
            // then
            assertThat(recorder.getError()).isNull();
        }

        @Test
        void shouldReturnNameOfTheBookThatWasProvidedInPayload() throws Exception {
            // given
            DefaultGrpcBookService testable = TestableBuilder.builder()
                    .withBooks(EXISTING_BOOK)
                    .build();

            UpdateBookRequest updateBookRequest = UpdateBookRequestFaker.withId(EXISTING_BOOK.getId()).get();

            // when
            BookDto bookDto = updateBookRequestAndGetResponsePayload(testable, updateBookRequest);

            // then
            assertThat(bookDto.getName()).isEqualTo(updateBookRequest.getNewBook().getName());
        }

        @Test
        void shouldReturnAuthorOfTheBookThatWasProvidedInPayload() throws Exception {
            // given
            DefaultGrpcBookService testable = TestableBuilder.builder()
                    .withBooks(EXISTING_BOOK)
                    .build();

            UpdateBookRequest updateBookRequest = UpdateBookRequestFaker.withId(EXISTING_BOOK.getId()).get();

            // when
            BookDto bookDto = updateBookRequestAndGetResponsePayload(testable, updateBookRequest);

            // then
            assertThat(bookDto.getAuthor()).isEqualTo(updateBookRequest.getNewBook().getAuthor());
        }

        @Test
        void shouldReturnIsbnOfTheBookThatWasProvided() throws Exception {
            // given
            DefaultGrpcBookService testable = TestableBuilder.builder()
                    .withBooks(EXISTING_BOOK)
                    .build();

            UpdateBookRequest updateBookRequest = UpdateBookRequestFaker.withId(EXISTING_BOOK.getId()).get();

            // when
            BookDto bookDto = updateBookRequestAndGetResponsePayload(testable, updateBookRequest);

            // then
            assertThat(bookDto.getIsbn()).isEqualTo(updateBookRequest.getNewBook().getIsbn());
        }

        @Test
        void shouldReturnQuantityOfTheBookThatWasProvidedInPayload() throws Exception {
            // given
            DefaultGrpcBookService testable = TestableBuilder.builder()
                    .withBooks(EXISTING_BOOK)
                    .build();

            UpdateBookRequest updateBookRequest = UpdateBookRequestFaker.withId(EXISTING_BOOK.getId()).get();

            // when
            BookDto bookDto = updateBookRequestAndGetResponsePayload(testable, updateBookRequest);

            // then
            assertThat(bookDto.getQuantity()).isEqualTo(updateBookRequest.getNewBook().getQuantity());
        }

        @Test
        void shouldReturnIdOfTheBookThatWasRequested() throws Exception {
            // given
            DefaultGrpcBookService testable = TestableBuilder.builder()
                    .withBooks(EXISTING_BOOK)
                    .build();

            UpdateBookRequest updateBookRequest = UpdateBookRequestFaker.withId(EXISTING_BOOK.getId()).get();

            // when
            BookDto bookDto = updateBookRequestAndGetResponsePayload(testable, updateBookRequest);

            // then
            assertThat(bookDto.getId()).isEqualTo(updateBookRequest.getBookId());
        }

        @Test
        void shouldSaveUpdatedBook() throws Exception {
            DefaultGrpcBookService testable = TestableBuilder.builder()
                    .withBooks(EXISTING_BOOK)
                    .build();

            UpdateBookRequest updateBookRequest = UpdateBookRequestFaker.withId(EXISTING_BOOK.getId()).get();

            BookDto responsePayload = updateBookRequestAndGetResponsePayload(testable, updateBookRequest);

            BookDto foundBook = fetchBookAndGetResponsePayload(testable, responsePayload.getId());

            assertThat(responsePayload).isEqualTo(foundBook);
        }

        @Test
        void shouldThrowErrorIfBookByIdDoesNotExist() throws Exception {
            // given
            DefaultGrpcBookService testable = TestableBuilder.builder()
                    .withBooks(EXISTING_BOOK)
                    .build();

            UpdateBookRequest updateBookRequest = UpdateBookRequestFaker.withId(UUID.randomUUID()).get();
            // when
            StreamRecorder<BookDto> response = updateBookRequest(testable, updateBookRequest);

            // then
            assertThat(response.getError()).isInstanceOf(BookNotFoundException.class);
        }


        private BookDto fetchBookAndGetResponsePayload(DefaultGrpcBookService testable, String bookId) throws Exception {
            return fetchBook(testable, bookId).firstValue().get();
        }

        private StreamRecorder<BookDto> fetchBook(DefaultGrpcBookService testable, String bookId) throws Exception {
            StreamRecorder<BookDto> fetchBookRecorder = StreamRecorder.create();

            FetchBookRequest fetchBookRequest = FetchBookRequest.newBuilder().setBookId(bookId).build();

            testable.fetchBook(fetchBookRequest, fetchBookRecorder);

            fetchBookRecorder.awaitCompletion(5, TimeUnit.SECONDS);

            return fetchBookRecorder;
        }

        private StreamRecorder<BookDto> updateBookRequest(DefaultGrpcBookService testable, UpdateBookRequest updateBookRequest) throws Exception {
            StreamRecorder<BookDto> recorder = StreamRecorder.create();

            testable.updateBook(updateBookRequest, recorder);

            recorder.awaitCompletion(5, TimeUnit.SECONDS);
            return recorder;
        }

        private BookDto updateBookRequestAndGetResponsePayload(DefaultGrpcBookService testable, UpdateBookRequest updateBookRequest) throws Exception {
            return updateBookRequest(testable, updateBookRequest).firstValue().get();
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
                    new BookDtoConverterImpl(),
                    new CreateBookInfoConverterImpl(),
                    new UpdateBookInfoConverterImpl());
        }
    }
}