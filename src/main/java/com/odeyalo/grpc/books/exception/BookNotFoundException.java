package com.odeyalo.grpc.books.exception;

import com.odeyalo.grpc.books.client.book.Book;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.ProtoUtils;
import org.jetbrains.annotations.Nullable;

public final class BookNotFoundException extends StatusRuntimeException {
    private static final Status DEFAULT_STATUS = Status.NOT_FOUND;
    private static final String DEFAULT_MESSAGE = "Requested Book cannot be found.";
    private static final Metadata DEFAULT_METADATA = new Metadata();

    static {
        Metadata.Key<Book.ErrorResponse> key = ProtoUtils.keyForProto(Book.ErrorResponse.getDefaultInstance());
        Book.ErrorResponse errorResponse = Book.ErrorResponse.newBuilder().setMessage(DEFAULT_MESSAGE).build();

        DEFAULT_METADATA.put(key, errorResponse);
    }

    private BookNotFoundException() {
        super(DEFAULT_STATUS, DEFAULT_METADATA);
    }

    private BookNotFoundException(Status status) {
        super(status);
    }

    private BookNotFoundException(Status status, @Nullable Metadata trailers) {
        super(status, trailers);
    }

    public static BookNotFoundException defaultException() {
        return new BookNotFoundException();
    }

    public static BookNotFoundException withCustomStatus(Status status) {
        return new BookNotFoundException(status);
    }

    public static BookNotFoundException withMetadata(Status status, Metadata metadata) {
        return new BookNotFoundException(status, metadata);
    }
}
