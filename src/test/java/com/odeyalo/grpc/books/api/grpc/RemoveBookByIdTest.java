package com.odeyalo.grpc.books.api.grpc;

import com.odeyalo.grpc.books.entity.BookEntity;
import io.grpc.internal.testing.StreamRecorder;
import org.junit.jupiter.api.Test;
import testing.faker.BookEntityFaker;

import static com.odeyalo.grpc.books.api.grpc.Book.DeletionStatus.SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static testing.factory.DefaultGrpcBookServiceTestableBuilder.testableBuilder;

class RemoveBookByIdTest extends AbstractBookClientTest {
    public static final BookEntity EXISTING_BOOK = BookEntityFaker.create().get();

    @Test
    void shouldCompleteWithoutError() throws Exception {
        final DefaultGrpcBookService testable = testableBuilder()
                .withBooks(EXISTING_BOOK)
                .build();

        final StreamRecorder<Book.DeleteBookResponse> streamRecorder = removeBook(testable, EXISTING_BOOK.getId());

        assertThat(streamRecorder.getError()).isNull();
    }

    @Test
    void shouldReturnSuccessCompletionStatusIfBookExist() throws Exception {
        final DefaultGrpcBookService testable = testableBuilder()
                .withBooks(EXISTING_BOOK)
                .build();

        final Book.DeleteBookResponse bookDeleteResponse = removeBookAndGetBody(testable, EXISTING_BOOK.getId());

        assertThat(bookDeleteResponse.getStatus()).isEqualTo(SUCCESS);
    }

    @Test
    void shouldActuallyDeleteTheBookFromService() throws Exception {
        final DefaultGrpcBookService testable = testableBuilder()
                .withBooks(EXISTING_BOOK)
                .build();

        final Book.DeleteBookResponse ignored = removeBookAndGetBody(testable, EXISTING_BOOK.getId());

        StreamRecorder<Book.BookDto> recorder = fetchBook(testable, EXISTING_BOOK.getId().toString());

        assertThat(recorder.getValues()).isEmpty();
    }
}
