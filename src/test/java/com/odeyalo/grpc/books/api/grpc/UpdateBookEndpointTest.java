package com.odeyalo.grpc.books.api.grpc;

import com.odeyalo.grpc.books.api.grpc.Book.BookDto;
import com.odeyalo.grpc.books.api.grpc.Book.UpdateBookRequest;
import com.odeyalo.grpc.books.api.grpc.Book.UpdateBookRequest.UpdateBookPayload;
import com.odeyalo.grpc.books.entity.BookEntity;
import com.odeyalo.grpc.books.exception.BookNotFoundException;
import com.odeyalo.grpc.books.exception.RequestValidationException;
import io.grpc.internal.testing.StreamRecorder;
import org.junit.jupiter.api.Test;
import testing.faker.BookEntityFaker;
import testing.faker.CreateBookRequestFaker;
import testing.faker.UpdateBookPayloadFaker;
import testing.faker.UpdateBookRequestFaker;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static testing.factory.DefaultGrpcBookServiceTestableBuilder.testableBuilder;

class UpdateBookEndpointTest extends AbstractBookClientTest {
    public static final BookEntity EXISTING_BOOK = BookEntityFaker.create().get();

    @Test
    void shouldCompleteWithoutAnyError() throws Exception {
        // given
        DefaultGrpcBookService testable = testableBuilder()
                .withBooks(EXISTING_BOOK)
                .build();
        UpdateBookRequest updateBookRequest = UpdateBookRequestFaker.withId(EXISTING_BOOK.getId()).get();

        // when
        StreamRecorder<BookDto> recorder = updateBookRequest(testable, updateBookRequest);
        // then
        assertThat(recorder.getError()).isNull();
    }

    @Test
    void shouldReturnValidationErrorIfIdIsNotUUID() throws Exception {
        // given
        DefaultGrpcBookService testable = testableBuilder().build();
        UpdateBookRequest updateBookRequest = UpdateBookRequest.newBuilder().setBookId("123").build();
        // when
        StreamRecorder<BookDto> recorder = updateBookRequest(testable, updateBookRequest);
        // then
        assertThat(recorder.getError()).isInstanceOf(RequestValidationException.class);
    }

    @Test
    void shouldReturnErrorIfBookNameIsLessThan1Symbol() throws Exception {
        // given
        DefaultGrpcBookService testable = testableBuilder().build();
        UpdateBookPayload payload = UpdateBookPayloadFaker.create().setTitle("").get();

        UpdateBookRequest malformedUpdateBookRequest = UpdateBookRequest.newBuilder()
                .setBookId(EXISTING_BOOK.getId().toString())
                .setNewBook(payload)
                .build();
        // when
        StreamRecorder<Book.BookDto> recorder = updateBookRequest(testable, malformedUpdateBookRequest);

        // then
        assertThat(recorder.getError()).isInstanceOf(RequestValidationException.class);
    }

    @Test
    void shouldReturnErrorIfBookIsbnIsLessThan10Symbols() throws Exception {
        // given
        DefaultGrpcBookService testable = testableBuilder().build();
        UpdateBookPayload payload = UpdateBookPayloadFaker.create()
                .setIsbn("12345")
                .get();

        UpdateBookRequest malformedUpdateBookRequest = UpdateBookRequest.newBuilder()
                .setBookId(EXISTING_BOOK.getId().toString())
                .setNewBook(payload)
                .build();
        // when
        StreamRecorder<Book.BookDto> recorder = updateBookRequest(testable, malformedUpdateBookRequest);

        // then
        assertThat(recorder.getError()).isInstanceOf(RequestValidationException.class);
    }

    @Test
    void shouldReturnNameOfTheBookThatWasProvidedInPayload() throws Exception {
        // given
        DefaultGrpcBookService testable = testableBuilder()
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
        DefaultGrpcBookService testable = testableBuilder()
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
        DefaultGrpcBookService testable = testableBuilder()
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
        DefaultGrpcBookService testable = testableBuilder()
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
        DefaultGrpcBookService testable = testableBuilder()
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
        DefaultGrpcBookService testable = testableBuilder()
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
        DefaultGrpcBookService testable = testableBuilder()
                .withBooks(EXISTING_BOOK)
                .build();

        UpdateBookRequest updateBookRequest = UpdateBookRequestFaker.withId(UUID.randomUUID()).get();
        // when
        StreamRecorder<BookDto> response = updateBookRequest(testable, updateBookRequest);

        // then
        assertThat(response.getError()).isInstanceOf(BookNotFoundException.class);
    }
}
