package com.odeyalo.grpc.books.api.grpc;

import io.grpc.internal.testing.StreamRecorder;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

abstract class AbstractBookClientTest {

    protected Book.BookDto saveBook(DefaultGrpcBookService testable, Book.CreateBookRequest createBookRequest) throws Exception {
        StreamRecorder<Book.BookDto> recorder = StreamRecorder.create();

        testable.addBook(createBookRequest, recorder);

        recorder.awaitCompletion(5, TimeUnit.SECONDS);

        return recorder.firstValue().get();
    }

    protected Book.BookDto fetchBookAndGetResponsePayload(DefaultGrpcBookService testable, String bookId) throws Exception {
        return fetchBook(testable, bookId).firstValue().get();
    }

    protected StreamRecorder<Book.BookDto> fetchBook(DefaultGrpcBookService testable, String bookId) throws Exception {
        StreamRecorder<Book.BookDto> fetchBookRecorder = StreamRecorder.create();

        Book.FetchBookRequest fetchBookRequest = Book.FetchBookRequest.newBuilder().setBookId(bookId).build();

        testable.fetchBook(fetchBookRequest, fetchBookRecorder);

        fetchBookRecorder.awaitCompletion(5, TimeUnit.SECONDS);

        return fetchBookRecorder;
    }

    protected StreamRecorder<Book.BookDto> updateBookRequest(DefaultGrpcBookService testable, Book.UpdateBookRequest updateBookRequest) throws Exception {
        StreamRecorder<Book.BookDto> recorder = StreamRecorder.create();

        testable.updateBook(updateBookRequest, recorder);

        recorder.awaitCompletion(5, TimeUnit.SECONDS);
        return recorder;
    }

    protected Book.BookDto updateBookRequestAndGetResponsePayload(DefaultGrpcBookService testable, Book.UpdateBookRequest updateBookRequest) throws Exception {
        return updateBookRequest(testable, updateBookRequest).firstValue().get();
    }

    protected Book.DeleteBookResponse removeBookAndGetBody(DefaultGrpcBookService testable, UUID id) throws Exception {
        return removeBook(testable, id).firstValue().get();
    }

    protected StreamRecorder<Book.DeleteBookResponse> removeBook(DefaultGrpcBookService testable, UUID id) throws Exception {
        Book.DeleteBookRequest deleteBookRequest = Book.DeleteBookRequest.newBuilder()
                .setBookId(id.toString())
                .build();

        final StreamRecorder<Book.DeleteBookResponse> streamRecorder = StreamRecorder.create();
        testable.removeBook(deleteBookRequest, streamRecorder);

        streamRecorder.awaitCompletion(5, TimeUnit.SECONDS);

        return streamRecorder;
    }
}
