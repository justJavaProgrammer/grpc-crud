package com.odeyalo.grpc.books.api.grpc;

import com.google.protobuf.Message;
import com.odeyalo.grpc.books.exception.BookNotFoundException;
import com.odeyalo.grpc.books.exception.RequestValidationException;
import com.odeyalo.grpc.books.service.BookService;
import com.odeyalo.grpc.books.support.converter.BookDtoConverter;
import com.odeyalo.grpc.books.support.validation.ReactiveGrpcRequestValidator;
import org.jetbrains.annotations.NotNull;
import org.lognet.springboot.grpc.GRpcService;
import reactor.core.publisher.Mono;

import java.util.UUID;

@GRpcService
public final class DefaultReactiveBookService extends ReactorBookServiceGrpc.BookServiceImplBase {
    private final ReactiveGrpcRequestValidator grpcRequestValidator;
    private final BookService bookService;
    private final BookDtoConverter bookDtoConverter;

    public DefaultReactiveBookService(ReactiveGrpcRequestValidator grpcRequestValidator, BookService bookService, BookDtoConverter bookDtoConverter) {
        this.grpcRequestValidator = grpcRequestValidator;
        this.bookService = bookService;
        this.bookDtoConverter = bookDtoConverter;
    }

    @Override
    public Mono<Book.BookDto> fetchBook(Book.FetchBookRequest request) {
        return validate(request)
                .flatMap(it -> bookService.findBookById(UUID.fromString(request.getBookId())))
                .map(bookDtoConverter::toBookDto)
                .switchIfEmpty(Mono.defer(() -> Mono.error(BookNotFoundException.defaultException())));
    }


    @NotNull
    private <T extends Message> Mono<@NotNull T> validate(@NotNull T request) {
        return grpcRequestValidator.validate(request)
                .flatMap(it -> {
                    if ( it.isSuccess() ) {
                        return Mono.just(request);
                    }
                    return Mono.error(RequestValidationException.withViolations(it.getViolations()));
                });
    }
}
