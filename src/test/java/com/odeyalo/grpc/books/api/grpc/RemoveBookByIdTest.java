package com.odeyalo.grpc.books.api.grpc;

import com.odeyalo.grpc.books.api.grpc.Book.DeleteBookRequest;
import com.odeyalo.grpc.books.entity.BookEntity;
import io.grpc.internal.testing.StreamRecorder;
import org.junit.jupiter.api.Test;
import testing.faker.BookEntityFaker;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static testing.factory.DefaultGrpcBookServiceTestableBuilder.testableBuilder;

class RemoveBookByIdTest extends AbstractBookClientTest {
    public static final BookEntity EXISTING_BOOK = BookEntityFaker.create().get();

    @Test
    void shouldCompleteWithoutError() throws Exception {
        final DefaultGrpcBookService testable = testableBuilder()
                .withBooks(EXISTING_BOOK)
                .build();

        final DeleteBookRequest deleteBookRequest = DeleteBookRequest.newBuilder()
                .setBookId(EXISTING_BOOK.getId().toString())
                .build();

        final StreamRecorder<Book.DeleteBookResponse> streamRecorder = StreamRecorder.create();
        testable.removeBook(deleteBookRequest, streamRecorder);

        streamRecorder.awaitCompletion(5, TimeUnit.SECONDS);

        assertThat(streamRecorder.getError()).isNull();
    }
}
