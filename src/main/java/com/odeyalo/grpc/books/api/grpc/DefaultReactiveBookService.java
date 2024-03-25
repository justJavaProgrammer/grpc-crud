package com.odeyalo.grpc.books.api.grpc;

import com.google.protobuf.Message;
import com.odeyalo.grpc.books.exception.BookNotFoundException;
import com.odeyalo.grpc.books.exception.RequestValidationException;
import com.odeyalo.grpc.books.service.BookService;
import com.odeyalo.grpc.books.support.converter.BookDtoConverter;
import com.odeyalo.grpc.books.support.converter.CreateBookInfoConverter;
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
    private final CreateBookInfoConverter createBookInfoConverter;

    public DefaultReactiveBookService(ReactiveGrpcRequestValidator grpcRequestValidator,
                                      BookService bookService,
                                      BookDtoConverter bookDtoConverter,
                                      CreateBookInfoConverter createBookInfoConverter) {
        this.grpcRequestValidator = grpcRequestValidator;
        this.bookService = bookService;
        this.bookDtoConverter = bookDtoConverter;
        this.createBookInfoConverter = createBookInfoConverter;
    }

    @Override
    public Mono<Book.BookDto> fetchBook(Book.FetchBookRequest request) {
        return validate(request)
                .flatMap(it -> bookService.findBookById(UUID.fromString(request.getBookId())))
                .map(bookDtoConverter::toBookDto)
                .switchIfEmpty(Mono.defer(() -> Mono.error(BookNotFoundException.defaultException())));
    }

    @Override
    public Mono<Book.BookDto> addBook(Book.CreateBookRequest request) {
        return validate(request)
                .map(createBookInfoConverter::toCreateBookInfo)
                .flatMap(it -> bookService.save(it).map(bookDtoConverter::toBookDto));
    }

    @Override
    public Mono<Book.DeleteBookResponse> removeBook(Book.DeleteBookRequest request) {
        return validate(request)
                .flatMap(it -> bookService.removeById(UUID.fromString(request.getBookId())))
                .thenReturn(successDeleteResponse());
    }

    @NotNull
    private static Book.DeleteBookResponse successDeleteResponse() {
        return Book.DeleteBookResponse.newBuilder()
                .setStatus(Book.DeletionStatus.SUCCESS)
                .build();
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
