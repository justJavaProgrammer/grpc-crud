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

//    @Test
//    void shouldReturnAuthorOfTheBookThatWasProvidedInPayload() throws Exception {
//        // given
//        DefaultGrpcBookService testable = testableBuilder()
//                .withBooks(EXISTING_BOOK)
//                .build();
//
//        UpdateBookRequest updateBookRequest = UpdateBookRequestFaker.withId(EXISTING_BOOK.getId()).get();
//
//        // when
//        BookDto bookDto = updateBookRequestAndGetResponsePayload(testable, updateBookRequest);
//
//        // then
//        assertThat(bookDto.getAuthor()).isEqualTo(updateBookRequest.getNewBook().getAuthor());
//    }
//
//    @Test
//    void shouldReturnIsbnOfTheBookThatWasProvided() throws Exception {
//        // given
//        DefaultGrpcBookService testable = testableBuilder()
//                .withBooks(EXISTING_BOOK)
//                .build();
//
//        UpdateBookRequest updateBookRequest = UpdateBookRequestFaker.withId(EXISTING_BOOK.getId()).get();
//
//        // when
//        BookDto bookDto = updateBookRequestAndGetResponsePayload(testable, updateBookRequest);
//
//        // then
//        assertThat(bookDto.getIsbn()).isEqualTo(updateBookRequest.getNewBook().getIsbn());
//    }
//
//    @Test
//    void shouldReturnQuantityOfTheBookThatWasProvidedInPayload() throws Exception {
//        // given
//        DefaultGrpcBookService testable = testableBuilder()
//                .withBooks(EXISTING_BOOK)
//                .build();
//
//        UpdateBookRequest updateBookRequest = UpdateBookRequestFaker.withId(EXISTING_BOOK.getId()).get();
//
//        // when
//        BookDto bookDto = updateBookRequestAndGetResponsePayload(testable, updateBookRequest);
//
//        // then
//        assertThat(bookDto.getQuantity()).isEqualTo(updateBookRequest.getNewBook().getQuantity());
//    }
//
//    @Test
//    void shouldReturnIdOfTheBookThatWasRequested() throws Exception {
//        // given
//        DefaultGrpcBookService testable = testableBuilder()
//                .withBooks(EXISTING_BOOK)
//                .build();
//
//        UpdateBookRequest updateBookRequest = UpdateBookRequestFaker.withId(EXISTING_BOOK.getId()).get();
//
//        // when
//        BookDto bookDto = updateBookRequestAndGetResponsePayload(testable, updateBookRequest);
//
//        // then
//        assertThat(bookDto.getId()).isEqualTo(updateBookRequest.getBookId());
//    }
//
//    @Test
//    void shouldSaveUpdatedBook() throws Exception {
//        DefaultGrpcBookService testable = testableBuilder()
//                .withBooks(EXISTING_BOOK)
//                .build();
//
//        UpdateBookRequest updateBookRequest = UpdateBookRequestFaker.withId(EXISTING_BOOK.getId()).get();
//
//        BookDto responsePayload = updateBookRequestAndGetResponsePayload(testable, updateBookRequest);
//
//        BookDto foundBook = fetchBookAndGetResponsePayload(testable, responsePayload.getId());
//
//        assertThat(responsePayload).isEqualTo(foundBook);
//    }
//
//    @Test
//    void shouldThrowErrorIfBookByIdDoesNotExist() throws Exception {
//        // given
//        DefaultGrpcBookService testable = testableBuilder()
//                .withBooks(EXISTING_BOOK)
//                .build();
//
//        UpdateBookRequest updateBookRequest = UpdateBookRequestFaker.withId(UUID.randomUUID()).get();
//        // when
//        StreamRecorder<BookDto> response = updateBookRequest(testable, updateBookRequest);
//
//        // then
//        assertThat(response.getError()).isInstanceOf(BookNotFoundException.class);
//    }
}
