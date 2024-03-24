package com.odeyalo.grpc.books.exception;

import build.buf.validate.Violation;
import com.odeyalo.grpc.books.api.grpc.Book;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.ProtoUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RequestValidationException extends StatusRuntimeException {
    /**
     * Internal errors. This means that some invariants expected by the underlying system have been broken. This error code is reserved for serious errors.
     * Using INTERNAL because it is written in docs
     * <p>
     * Error parsing request proto
     *
     * @see <a href="https://grpc.github.io/grpc/core/md_doc_statuscodes.html">Error codes</a>
     */
    private static final Status STATUS = Status.INTERNAL;

    public RequestValidationException() {
        super(STATUS);
    }

    private RequestValidationException(Status status) {
        super(status);
    }

    public RequestValidationException(@Nullable Metadata trailers) {
        super(STATUS, trailers);
    }

    public static RequestValidationException defaultException() {
        return new RequestValidationException();
    }

    public static RequestValidationException withViolations(List<Violation> violations) {
        if ( violations.isEmpty() ) {
            return defaultException();
        }

        Violation firstViolation = violations.get(0);

        Status firstViolationStatus = STATUS.withDescription(firstViolation.getFieldPath() + " " + firstViolation.getMessage());

        return new RequestValidationException(firstViolationStatus);
    }
}
