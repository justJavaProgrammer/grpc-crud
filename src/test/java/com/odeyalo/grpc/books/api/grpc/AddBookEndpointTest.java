package com.odeyalo.grpc.books.api.grpc;

import com.odeyalo.grpc.books.exception.RequestValidationException;
import io.grpc.internal.testing.StreamRecorder;
import org.junit.jupiter.api.Test;
import testing.faker.CreateBookRequestFaker;

import static org.assertj.core.api.Assertions.assertThat;
import static testing.factory.DefaultGrpcBookServiceTestableBuilder.testableBuilder;

class AddBookEndpointTest extends AbstractBookClientTest {
    public static final Book.CreateBookRequest CREATE_NOVEL_REQUEST = Book.CreateBookRequest.newBuilder()
            .setName("Three Days of Happiness")
            .setIsbn("12345677899")
            .setAuthor("Sugaru Miaki")
            .setQuantity(10)
            .build();

    @Test
    void shouldReturnSavedBookWithGeneratedId() throws Exception {
        DefaultGrpcBookService testable = testableBuilder().build();
        // when
        Book.BookDto book = saveBook(testable);
        // then
        assertThat(book.getId()).isNotEmpty();
    }

    @Test
    void shouldReturnErrorIfBookNameIsLessThan1Symbol() throws Exception {
        // given
        DefaultGrpcBookService testable = testableBuilder().build();

        Book.CreateBookRequest bookRequest = CreateBookRequestFaker.create()
                .setTitle("")
                .get();
        // when
        StreamRecorder<Book.BookDto> recorder = saveBook(testable, bookRequest);

        // then
        assertThat(recorder.getError()).isInstanceOf(RequestValidationException.class);
    }

    @Test
    void shouldReturnErrorIfBookIsbnIsLessThan10Symbols() throws Exception {
        // given
        DefaultGrpcBookService testable = testableBuilder().build();

        Book.CreateBookRequest bookRequest = CreateBookRequestFaker.create()
                .setIsbn("less")
                .get();
        // when
        StreamRecorder<Book.BookDto> recorder = saveBook(testable, bookRequest);

        // then
        assertThat(recorder.getError()).isInstanceOf(RequestValidationException.class);
    }

    @Test
    void shouldReturnErrorIfBookAuthorNameIsLessThan5Symbols() throws Exception {
        // given
        DefaultGrpcBookService testable = testableBuilder().build();

        Book.CreateBookRequest bookRequest = CreateBookRequestFaker.create()
                .setAuthorName("less")
                .get();
        // when
        StreamRecorder<Book.BookDto> recorder = saveBook(testable, bookRequest);

        // then
        assertThat(recorder.getError()).isInstanceOf(RequestValidationException.class);
    }

    @Test
    void shouldReturnSavedBookWithTitleAsProvided() throws Exception {
        DefaultGrpcBookService testable = testableBuilder().build();
        // when
        Book.BookDto book = saveBook(testable);
        // then
        assertThat(book.getName()).isEqualTo(CREATE_NOVEL_REQUEST.getName());
    }

    @Test
    void shouldReturnSavedBookWithIsbnAsProvided() throws Exception {
        DefaultGrpcBookService testable = testableBuilder().build();
        // when
        Book.BookDto book = saveBook(testable);
        // then
        assertThat(book.getIsbn()).isEqualTo(CREATE_NOVEL_REQUEST.getIsbn());
    }

    @Test
    void shouldReturnSavedBookWithAuthorAsProvided() throws Exception {
        DefaultGrpcBookService testable = testableBuilder().build();
        // when
        Book.BookDto book = saveBook(testable);
        // then
        assertThat(book.getAuthor()).isEqualTo(CREATE_NOVEL_REQUEST.getAuthor());
    }

    @Test
    void shouldReturnSavedBookWithQuantityAsProvided() throws Exception {
        DefaultGrpcBookService testable = testableBuilder().build();
        // when
        Book.BookDto book = saveBook(testable);
        // then
        assertThat(book.getQuantity()).isEqualTo(CREATE_NOVEL_REQUEST.getQuantity());
    }

    @Test
    void bookShouldBeFoundAfterSave() throws Exception {
        DefaultGrpcBookService testable = testableBuilder().build();
        // when
        Book.BookDto savedBook = saveBook(testable);

        // then
        Book.BookDto bookDto = fetchBookAndGetResponsePayload(testable, savedBook.getId());

        assertThat(bookDto).isEqualTo(savedBook);
    }

    private Book.BookDto saveBook(DefaultGrpcBookService testable) throws Exception {
        return saveBookAngGetBody(testable, CREATE_NOVEL_REQUEST);
    }
}
