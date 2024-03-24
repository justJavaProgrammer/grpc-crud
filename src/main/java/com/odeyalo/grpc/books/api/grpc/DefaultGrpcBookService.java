package com.odeyalo.grpc.books.api.grpc;

import com.google.protobuf.Message;
import com.odeyalo.grpc.books.api.grpc.Book.*;
import com.odeyalo.grpc.books.exception.BookNotFoundException;
import com.odeyalo.grpc.books.exception.RequestValidationException;
import com.odeyalo.grpc.books.model.Book;
import com.odeyalo.grpc.books.service.BookService;
import com.odeyalo.grpc.books.service.UpdateBookInfo;
import com.odeyalo.grpc.books.support.converter.BookDtoConverter;
import com.odeyalo.grpc.books.support.converter.CreateBookInfoConverter;
import com.odeyalo.grpc.books.support.converter.UpdateBookInfoConverter;
import com.odeyalo.grpc.books.support.validation.ReactiveGrpcRequestValidator;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@GrpcService
public final class DefaultGrpcBookService extends BookServiceGrpc.BookServiceImplBase {
    private final BookService bookService;
    private final ReactiveGrpcRequestValidator grpcRequestValidator;
    private final BookDtoConverter bookDtoConverter;
    private final CreateBookInfoConverter createBookInfoConverter;
    private final UpdateBookInfoConverter updateBookInfoConverter;

    public DefaultGrpcBookService(BookService bookService,
                                  ReactiveGrpcRequestValidator grpcRequestValidator,
                                  BookDtoConverter bookDtoConverter,
                                  CreateBookInfoConverter createBookInfoConverter,
                                  UpdateBookInfoConverter updateBookInfoConverter) {
        this.bookService = bookService;
        this.grpcRequestValidator = grpcRequestValidator;
        this.bookDtoConverter = bookDtoConverter;
        this.createBookInfoConverter = createBookInfoConverter;
        this.updateBookInfoConverter = updateBookInfoConverter;
    }

    @Override
    public void fetchBook(@NotNull final FetchBookRequest request,
                          @NotNull final StreamObserver<BookDto> responseObserver) {

        validate(request)
                .flatMap(it -> doFetchBook(request)
                        .map(bookDtoConverter::toBookDto)
                        .switchIfEmpty(Mono.defer(() -> Mono.error(BookNotFoundException.defaultException()))))
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(responseObserver::onNext, responseObserver::onError, responseObserver::onCompleted);

    }

    @Override
    public void addBook(CreateBookRequest request, StreamObserver<BookDto> responseObserver) {

        validate(request)
                .map(createBookInfoConverter::toCreateBookInfo)
                .flatMap(it -> bookService.save(it).map(bookDtoConverter::toBookDto))
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(responseObserver::onNext, responseObserver::onError, responseObserver::onCompleted);
    }

    @Override
    public void updateBook(UpdateBookRequest request, StreamObserver<BookDto> responseObserver) {
        validate(request)
                .map(this::toUpdateBookInfo)
                .flatMap(it -> bookService
                        .updateBook(UUID.fromString(request.getBookId()), it)
                        .onErrorMap(error -> BookNotFoundException.defaultException())
                        .map(bookDtoConverter::toBookDto))
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(responseObserver::onNext, responseObserver::onError, responseObserver::onCompleted);
    }

    @Override
    public void removeBook(DeleteBookRequest request, StreamObserver<DeleteBookResponse> responseObserver) {

        validate(request)
                .flatMap(it -> bookService.removeById(UUID.fromString(request.getBookId()))
                        .thenReturn(successDeleteResponse()))
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(responseObserver::onNext, responseObserver::onError, responseObserver::onCompleted);
    }

    @NotNull
    private <T extends Message> Mono<@NotNull T> validate(@NotNull T request) {

        return grpcRequestValidator.validate(request)
                .flatMap(it -> {
                    if ( it.isSuccess() ) {
                        return Mono.just(request);
                    }
                    return Mono.error(RequestValidationException.defaultException());
                });
    }

    @NotNull
    private static DeleteBookResponse successDeleteResponse() {
        return DeleteBookResponse.newBuilder()
                .setStatus(DeletionStatus.SUCCESS)
                .build();
    }

    @NotNull
    private UpdateBookInfo toUpdateBookInfo(UpdateBookRequest request) {
        return updateBookInfoConverter.toUpdateBookInfo(request.getNewBook());
    }

    @NotNull
    private Mono<Book> doFetchBook(@NotNull FetchBookRequest request) {
        return bookService.findBookById(UUID.fromString(request.getBookId()));
    }
}
