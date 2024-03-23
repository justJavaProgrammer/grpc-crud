package com.odeyalo.grpc.books.exception;

public final class BookUpdateFailedException extends RuntimeException {
    public static BookUpdateFailedException defaultException() {
        return new BookUpdateFailedException();
    }

    public static BookUpdateFailedException withCustomMessage(String message) {
        return new BookUpdateFailedException(message);
    }

    public static BookUpdateFailedException withMessageAndCause(String message, Throwable cause) {
        return new BookUpdateFailedException(message, cause);
    }

    public BookUpdateFailedException() {
        super();
    }

    public BookUpdateFailedException(String message) {
        super(message);
    }

    public BookUpdateFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookUpdateFailedException(Throwable cause) {
        super(cause);
    }
}
